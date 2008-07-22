/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.reload;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.sf.click.service.ClickClickConfigService;
import net.sf.click.service.ConfigService;
import net.sf.click.util.ClickUtils;

/**
 * This Filter allows changes to class and resource bundles to be picked up
 * without restarting the web application.
 * <p/>
 * <b>Please note:</b> this filter is only enabled in the following modes:
 * <ul>
 * <li>Development</li>
 * <li>Debug</li>
 * <li>Trace</li>
 * </ul>
 * 
 * This feature is made possible by replacing the context class loader 
 * with an instance of {@link ReloadableClassLoader} for each incoming request.
 *
 * Bob Schellink
 */
public class ReloadClassFilter implements Filter {

    // -------------------------------------------------------- Constants

    private static final String INCLUDED_PACKAGES = "included-packages";

    private static final String CLASSPATH = "classpath";

    // -------------------------------------------------------- Variables

    /** The application configuration service. */
    protected ClickClickConfigService clickClickConfigService;

    private ClassLoader reloadableClassLoader = null;

    private URL[] classpath = null;

    private List includedPackagesList = new ArrayList();

    private List initialClasspath = new ArrayList();

    /** The filter has been configured flag. */
    private boolean configured = false;

    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    // --------------------------------------------------------- Public Methods

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (!configured) {
            loadConfiguration();
        }

        if (clickClickConfigService.isProductionMode() ||
            clickClickConfigService.isProfileMode()) {

            // In production modes skip processing
            chain.doFilter(request, response);
        } else {
            // In developments modes use custom request handler
            handleRequest(request, response, chain);
        }
    }

    /**
     * Take this filter out of service.
     *
     * @see Filter#destroy()
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Initialize the filter.
     *
     * @see Filter#init(FilterConfig)
     *
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        String includedPackages = filterConfig.getInitParameter(
            INCLUDED_PACKAGES);
        if (includedPackages != null) {
            StringTokenizer tokens = new StringTokenizer(includedPackages,
                ", \n\t");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                includedPackagesList.add(token);
            }
        }

        String classpathParams = filterConfig.getInitParameter(CLASSPATH);
        if (classpathParams != null) {
            StringTokenizer tokens = new StringTokenizer(classpathParams,
                ", \n\t");
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                initialClasspath.add(token);
            }
        }
    }

    /**
     * Set filter configuration. This function is equivalent to init and is
     * required by Weblogic 6.1.
     *
     * @param filterConfig the filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        init(filterConfig);
    }

    /**
     * Return filter config. This is required by Weblogic 6.1
     *
     * @return the filter configuration
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    // -------------------------------------------------------- Protected Methods

    /**
     * Load the filters configuration and set the configured flat to true.
     */
    protected void loadConfiguration() {
        ServletContext servletContext = getFilterConfig().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);
        if (!(configService instanceof ClickClickConfigService)) {
            throw new IllegalStateException(
                "ReloadClassFilter can only be used " +
                "in conjuction with ClickClickConfigService. Please see the " +
                "ReloadClassFilter JavaDoc on how to setup the ClickClickConfigService.");
        }
        clickClickConfigService = (ClickClickConfigService) configService;

        // Add default package to the package list
        includedPackagesList.add(clickClickConfigService.getPagesPackage());
    }

    /**
     * Handles the request in development modes.
     * <p/>
     * This method uses the ReloadableClassLoader as returned by
     * {@link #createReloadClassLoader()}.
     * 
     * @param request
     * @param response
     * @param chain
     */
    protected void handleRequest(ServletRequest request, ServletResponse response,
        FilterChain chain) {

        // TODO should createReloadClassLoader be synchronized
        //synchronized (lock) {
        //    if(reloadableClassLoader == null) {
        reloadableClassLoader = createReloadClassLoader();
        //     }
        //}

        // Grab hold of the current context class loader
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            // Set the new context class loader
            Thread.currentThread().setContextClassLoader(reloadableClassLoader);
            chain.doFilter(request, response);
        } catch (Throwable t) {
            while (t instanceof ServletException) {
                t = ((ServletException) t).getRootCause();
            }
            clickClickConfigService.getLogService().error(
                "Could not handle request", t);
        } finally {
            // Restore the context class loader
            Thread.currentThread().setContextClassLoader(orig);
        }
    }

    protected ReloadableClassLoader createReloadClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        classpath = getClasspath();
        ReloadableClassLoader loader = new ReloadableClassLoader(classpath, parent);
        for (Iterator it = includedPackagesList.iterator(); it.hasNext();) {
            String packageName = (String) it.next();
            loader.addPackageToInclude(packageName);
        }
        return loader;
    }

    protected void addToClasspath(String path, Set classpath) {
        try {
            File f = new File(path);
            if (f.exists()) {
                classpath.add(f.getCanonicalFile().toURL());
            } else if (path.endsWith(".jar")) {
                // Check for jar under the WEB-INF/lib dir
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                URL url = filterConfig.getServletContext().getResource("/WEB-INF/lib" +
                    path);
                if (url != null) {
                    classpath.add(url);
                }
            }
        } catch (Exception ex) {
        }
    }

    // -------------------------------------------------------- Private Methods

    private URL[] getClasspath() {
        Set classpathSet = new LinkedHashSet();
        for (Iterator it = initialClasspath.iterator(); it.hasNext();) {
            String path = (String) it.next();
            addToClasspath(path, classpathSet);
        }
        classpathSet.addAll(extractUrlList(Thread.currentThread().
            getContextClassLoader()));
        return (URL[]) classpathSet.toArray(new URL[]{null});
    }

    private List extractUrlList(ClassLoader cl) {
        List urlList = new ArrayList();
        try {
            Enumeration en = cl.getResources("");
            while (en.hasMoreElements()) {
                Object url = en.nextElement();
                urlList.add(url);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return urlList;
    }
}

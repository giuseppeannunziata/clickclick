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
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * Bob Schellink
 */
public class ReloadClassFilter implements Filter {
    
    private DynamicClassLoader dynamicClassLoader = null;
    private URL[] classpath = null;
    private final Object lock = new Object();
    private List includedPackagesList = new ArrayList();
    private List initialClasspath = new ArrayList();
    
    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;

    // --------------------------------------------------------- Public Methods

    public ReloadClassFilter() {
        //This option will stop the scanning of other entries in the FileMonitor
        //to save some overhead.
        boolean stopScanningAfterFileChange = true;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //synchronized (lock) {
        //    if(dynamicClassLoader == null) {
        createDynamicClassLoader();
       //     }
        //}
        
        ClassLoader orig = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(dynamicClassLoader);
            chain.doFilter(request, response);
        } catch(Throwable t) {
            while (t instanceof ServletException) {
                t = ((ServletException) t).getRootCause();
            }
            t.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(orig);
        }
    }
    
    public void destroy() {
    }

    private static final String INCLUDED_PACKAGES = "included-packages";
    private static final String CLASSPATH = "classpath";
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String includedPackages = filterConfig.getInitParameter(INCLUDED_PACKAGES);
        if(includedPackages != null) {
            StringTokenizer tokens = new StringTokenizer(includedPackages, ", \n\t");
            while(tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                includedPackagesList.add(token);
            }
        }
        
        String classpathParams = filterConfig.getInitParameter(CLASSPATH);
        if(classpathParams != null) {
            StringTokenizer tokens = new StringTokenizer(classpathParams, ", \n\t");
            while(tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                initialClasspath.add(token);
            }
        }
    }
    
    public void createDynamicClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        classpath = getClasspath();
        //dynamicClassLoader = new DynamicClassLoader(classpath, parent, fileMonitor);
        dynamicClassLoader = new DynamicClassLoader(classpath, parent);
        for(Iterator it = includedPackagesList.iterator(); it.hasNext(); ) {
            String packageName = (String) it.next();
            dynamicClassLoader.addPackageToInclude(packageName);
        }
    }

    public URL[] getClasspath() {
        Set classpathSet = new LinkedHashSet();
        for(Iterator it = initialClasspath.iterator(); it.hasNext(); ) {
            String path = (String) it.next();
            addToClasspath(path, classpathSet);
        }
        classpathSet.addAll(extractUrlList(Thread.currentThread().getContextClassLoader()));
        return (URL[]) classpathSet.toArray(new URL[] {null});
    }
    
    protected void addToClasspath(String path, Set classpath) {
        try {
            File f = new File(path);
            if(f.exists()) {
                classpath.add(f.getCanonicalFile().toURL());
            } else if (path.endsWith(".jar")) {
                // Check for jars under the WEB-INF/lib dir
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                URL url = filterConfig.getServletContext().getResource("/WEB-INF/lib" + path);
                if (url != null) {
                    classpath.add(url);
                }
            }
        } catch (Exception ex) {
        }
    }
    
    public List extractUrlList(ClassLoader cl) {
        List urlList = new ArrayList();
        try {
            Enumeration en = cl.getResources("");
            while(en.hasMoreElements()) {
                Object url = en.nextElement();
                urlList.add(url);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return urlList;
    }
}

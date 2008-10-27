/*
 * Copyright 2002-2006 The Apache Software Foundation
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.reload;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.click.service.ConfigService;
import net.sf.click.util.ClickUtils;

/**
 * ClassLoader which enables specified classes to be reloaded without restarting
 * the web application.
 * <p/>
 * It does this by reversing the lookup order for classes.  First it checks for
 * classes locally before checking its parent loader.
 * <p/>
 * In addition it can be configured to include and exclude specific classes
 * and packages.
 * <p/>
 * <b>NOTE:</b> This class was adapted and modified from the Apache Cocoon<br>
 * implementation https://svn.apache.org/repos/asf/cocoon/tags/cocoon-2.2/cocoon-bootstrap/cocoon-bootstrap-1.0.0-M1/src/main/java/org/apache/cocoon/classloader/DefaultClassLoader.java
 * <p>
 * Articles of interest :
 * </p>
 * <ul>
 *      <li>http://tech.puredanger.com/2006/11/09/classloader/</li>
 *      <li>http://www.javaworld.com/javaworld/javaqa/2003-06/01-qa-0606-load.html</li>
 *      <li>http://www.javalobby.org/java/forums/t18345.html</li>
 * </ul>
 *
 * @author Bob Schellink
 */
public class ReloadClassLoader extends URLClassLoader {

    private List includes = new ArrayList();

    private List excludes = new ArrayList();
    
    private ConfigService configService;

    public ReloadClassLoader(URL[] classpath, ClassLoader parent,
        ConfigService configService) {
        super(classpath, parent);
        this.configService = configService;
    }

    public void addInclude(String include) {
        includes.add(include);
    }

    public void addExclude(String exclude) {
        excludes.add(exclude);
    }
    
    public void addURL(URL url) {
        super.addURL(url);
    }

    protected boolean shouldLoadClass(String name) {
        if(name == null || name.length() == 0) {
            return false;
        }

        // Automatically exclude these common classes
        if (name.startsWith("java.") || name.startsWith("javax.servlet")) {
            return false;
        }

        // First check if class is excluded
        for(Iterator it = excludes.iterator(); it.hasNext(); ) {
            String packageName = (String) it.next();
            if(name.startsWith(packageName)) {
                return false;
            }
        }

        // Next check if class is included
        for(Iterator it = includes.iterator(); it.hasNext(); ) {
            String packageName = (String) it.next();
            if(name.startsWith(packageName)) {
                return true;
            }
        }

        return false;
    }

    protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //First, check if the class has already been loaded
        Class c = findLoadedClass(name);

        if (c == null) {

            //If not loaded yet, check if this class's package is included in the list
            //of allowed packages
            if(shouldLoadClass(name)) {
                try {
                    c = findClass(name);
                    configService.getLogService().trace("   Reloaded class '"
                        + name + "'");
                } catch (ClassNotFoundException ex) {
                    if(getParent() == null) {
                        throw ex;
                    }
                }
            }

            if(c == null) {

                if(getParent() == null) {
                    throw new ClassNotFoundException(name);
                } else {

                    //The class was not loaded so delegate to parent class loader
                    c = getParent().loadClass(name);
                }
            }
        }
        
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
    
    public final URL getResource(String name) {
        //Try to find resource locally
        URL resource = findResource(name);
        
        if (resource == null) {
            //If not found try parent
            resource = getParent().getResource(name);
        }
        return resource;
    }
}

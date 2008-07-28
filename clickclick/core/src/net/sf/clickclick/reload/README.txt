#Setup automatic class reloading

1) For automatic class reloading to work you must replace ClickApp and ClickServlet that 
    is included in the click-core with the same named files in this package. An easy way to do
    this is to place these two files in the classpath before the regular click-core classes.
    

2) By default ReloadableClassLoader only reloads classes inside the package 
    specified by the Page package in click.xml.

    To enable dynamic replacement of classes you can programmatically call
    'ReloadableClassLoader.addPackageToInclude(String package)'.

    If you use the ReloadClassFilter, you can specify a comma seperated list of 
    packages and classes to be loaded at initialization time. By specifying the 
    initialization parameter 'includes', you can provide a list of packages
    and classes that will be added to the ReloadableClassLoader. In the same vain
    you can specify packages and classes to be excluded.

    Please note that excludes will override includes, so if you both exclude 
    and include the class com.mycorp.page.MyPage, it will be excluded.

    Below is an example web.xml snippet:

    <filter>
        <filter-name>reload-filter</filter-name>
        <filter-class>net.sf.click.extras.devel.ReloadClassFilter</filter-class>
        <init-param>
            <param-name> 
                includes
            </param-name>
            <param-value>
                com.mycorp.page, com.mycorp.controls.MyForm
            </param-value>
        </init-param>
        <init-param>
            <param-name> 
                excludes
            </param-name>
            <param-value>
                com.mycorp.page.account, com.mycorp.page.ProductPage
            </param-value>
        </init-param>
    </filter>
  
    Add the mapping:

    <filter-mapping>
        <filter-name>reload-filter</filter-name>
        <servlet-name>click-servlet</servlet-name>
    </filter-mapping>

    Lastly you need to use the following config service:

    <context-param>
        <param-name>config-service-class</param-name>
        <param-value>net.sf.click.service.ReloadConfigService</param-value>
    </context-param>

    The snippet above will add the packages 'net.sf.click.tests.reload' and
    'com.mycorp.test' to the ReloadableClassLoader's list of packages to load.

    You may also need to specify the directory for where the compiled classes
    can be found on the file system. By default the ReloadClassFilter will 
    add all the locations that is found in the current thread's classpath
    to the ReloadableClassLoader. Because ReloadableClassLoader extends URLClassLoader,
    if you need to add different locations you can programmatically call 
    'ReloadableClassLoader.addURL(URL url)'.

    By specifying the initializaiton parameter 'classpath', you can provide
    a comma seperated list of directories to be included at initialization 
    time. Continuing with the above example results in:

    <filter>
        <filter-name>reload-filter</filter-name>
        <filter-class>net.sf.click.extras.devel.ReloadClassFilter</filter-class>
        <init-param>
            <param-name> 
                includes
            </param-name>
            <param-value>
                com.mycorp.page, com.mycorp.controls.MyForm
            </param-value>
        </init-param>
        <init-param>
            <param-name> 
                classpath
            </param-name>
            <param-value>
                c:/myproject/build/classes c:/anotherproject/build/classes
            </param-value>
        </init-param>
    </filter>

3) Certain servlet containers have the ability to track changes to classes 
    and jars, and reload the entire web application when changes occur. You should 
    probably disable this feature in your container if you want automatic class 
    reloading to work. Otherwise instead of only reloading the changed class, the 
    servlet container will restart the entire application.

    For Tomcat you can disable this feature by adding the attribute 
    'reloadable="false"' to your context.xml file for example:

    <Context
        path="/click-test" 
        reloadable="false"
        antiJARLocking="true"/>

    IDE's such as Netbeans will also reload the entire web application when you 
    click the "Run" button. So instead of hitting "Run" just refresh the browser
    to reload classes.

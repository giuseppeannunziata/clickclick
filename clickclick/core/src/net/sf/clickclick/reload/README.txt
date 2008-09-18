#Setup automatic class reloading

1) By default ReloadClassLoader only reloads classes inside the packages 
    specified by the Page packages in click.xml.

    You can specify a comma seperated list of packages and classes to be loaded 
    at initialization time. By specifying the initialization parameter 'includes',
    you can provide a list of packages and classes that will be added to the 
    ReloadClassLoader. In the same vain you can specify packages and classes to
    be excluded.

    Please note that excludes will override includes, so if you both exclude 
    and include the class com.mycorp.page.MyPage, it will be excluded.

    Below is an example web.xml snippet:
    
    <!--
    Here we tell Click to use ClickClickConfigService instead of the default
    XmlConfigService. ClickClickConfigService is needed because XmlConfigService
    cache Page classes even in development mode while ClickClickConfigService 
    only caches in production modes.
    -->
    <context-param>
        <param-name>config-service-class</param-name>
        <param-value>net.sf.click.service.ClickClickConfigService</param-value>
    </context-param>

    <!--
    Setup the reload class filter. This filter will only reload classes
    in development modes.
    -->
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
                com.mycorp.page.account, com.mycorp.page.MyStatefulPage
            </param-value>
        </init-param>
    </filter>
  
    <filter-mapping>
        <filter-name>reload-filter</filter-name>
        <servlet-name>click-servlet</servlet-name>
    </filter-mapping>
  
    The snippet above will setup the filter to reload classes containing the package
    'com.mycorp.page'. The filter will also reload the control 'com.mycorp.controls.MyForm'.
 
    The filter will *not* reload class containing the package 'com.mycorp.page.account'.
    The page 'com.mycorp.page.MyStatefulPage' is also excluded from reloading.

2) Certain servlet containers have the ability to track changes to classes 
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

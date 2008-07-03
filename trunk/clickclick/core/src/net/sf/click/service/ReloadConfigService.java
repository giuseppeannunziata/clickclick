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
package net.sf.click.service;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

/**
 *
 * @author Bob Schellink
 */
public class ReloadConfigService extends XmlConfigService {

    private Map manualPageByPathMap = new HashMap();

    private Map manualPageByClassNameMap = new HashMap();

    private static final Object PAGE_LOAD_LOCK = new Object();

    public void buildManualPageMapping(Element pagesElm) throws ClassNotFoundException {
        if (isProductionMode() || isProfileMode()) {
            super.buildManualPageMapping(pagesElm);
            return;
        }

        List pageList = ClickUtils.getChildren(pagesElm, "page");

        if (!pageList.isEmpty() && getLogService().isDebugEnabled()) {
            getLogService().debug("click.xml pages:");
        }

        for (int i = 0; i < pageList.size(); i++) {
            Element pageElm = (Element) pageList.get(i);

            PageMetaData page = new PageMetaData(pageElm, pagesPackage,
                commonHeaders);
            manualPageByPathMap.put(page.getPath(), page);
        }
    }

    void buildAutoPageMapping(Element pagesElm) throws ClassNotFoundException {

        if(isProductionMode() || isProfileMode()) {
            //Build and cache in production modes.
            super.buildAutoPageMapping(pagesElm);
            return;
        }

        // Build list of automap path page class overrides
        excludesList.clear();
        for (Iterator i = ClickUtils.getChildren(pagesElm, "excludes").iterator();
             i.hasNext();) {

            excludesList.add(new XmlConfigService.ExcludesElm((Element) i.next()));
        }
    }

    public void buildClassMap() {
        if (isProductionMode() || isProfileMode()) {
            super.buildClassMap();
            return;
        }

        //Build pages by class map.  The difference between this method and ClickApps method
        //is that the key is a string not the actual class. Also only manually mapped
        //pages will be stored here. The automapped pages are looked up dynamically.

        // Build pages by className map
        for (Iterator i = pageByPathMap.values().iterator(); i.hasNext();) {
            PageMetaData page = (PageMetaData) i.next();

            Object value = manualPageByClassNameMap.get(page.pageClassName);

            if (value == null) {
                manualPageByClassNameMap.put(page.pageClassName, page);

            } else if (value instanceof List) {
                ((List) value).add(value);

            } else if (value instanceof PageMetaData) {
                List list = new ArrayList();
                list.add(value);
                list.add(page);
                manualPageByClassNameMap.put(page.pageClassName, list);

            } else {
                // should never occur
                throw new IllegalStateException();
            }
        }
    }

    private PageMetaData lookupManuallyStoredMetaData(String path) {
        //Try and load the manually mapped page
        PageMetaData page = (PageMetaData) manualPageByPathMap.get(path);
        if (page == null) {
            String jspPath = StringUtils.replace(path, ".htm", ".jsp");
            page = (PageMetaData) manualPageByPathMap.get(jspPath);
        }
        return page;
    }

    private PageMetaData lookupManuallyStoredMetaData(Class pageClass) {
        //Try and load the manually mapped page
        PageMetaData page = null;
        Object object = (PageMetaData) manualPageByClassNameMap.get(pageClass);
        if (object instanceof PageMetaData) {
            page = (PageMetaData) object;
            return page;

        } else if (object instanceof List) {
            String msg =
                "Page class resolves to multiple paths: " + pageClass.getName();
            throw new IllegalArgumentException(msg);

        }

        return page;
    }

    /**
     * @see ConfigService#getPageClass(String)
     *
     * @param path the page path
     * @return the page class for the given path or null if no class is found
     */
    public Class getPageClass(String path) {

        // If in production or profile mode.
        if (isProductionMode() || isProfileMode()) {
            return super.getPageClass(path);

        } else {
            // Else in development, debug or trace mode
            synchronized (PAGE_LOAD_LOCK) {

                //Try and load the manually mapped page first
                PageMetaData page = lookupManuallyStoredMetaData(path);

                if (page != null) {
                    try {
                        return ClickUtils.classForName(page.getPageClassName());
                    } catch (ClassNotFoundException ex) {
                    //ignore, this class is not available, so try and load it
                    //from the classpath
                    }
                }

                Class pageClass = null;

                try {
                    //Set resourcePaths = servletContext.getResourcePaths(path);
                    URL resource = getServletContext().getResource(path);

                    if (resource != null) {
                        pageClass = getPageClass(path, pagesPackage);
                    } else {
                    //No caching of this class or fields are done here.
                    }

                } catch (MalformedURLException ex) {
                //ignore, will return null
                }

                return pageClass;
            }
        }
    }

    public String getPagePath(Class pageClass) {
        //Try to lookup path from manually mapped pages first
        PageMetaData page = lookupManuallyStoredMetaData(pageClass);
        if (page != null) {
            return page.getPath();
        }

        //If not found we do a reverse algorithm lookup for the path
        return new PathLookupAlgorithm().getPagePath(pageClass);
    }

    public Map getPageHeaders(String path) {
        //Try and load the manually mapped page first
        PageMetaData page = lookupManuallyStoredMetaData(path);

        if (page != null) {
            return page.getHeaders();
        } else {
            //If path was not found in the manually loaded pages, return common headers
            return Collections.unmodifiableMap(commonHeaders);
        }
    }

    public Field getPageField(Class pageClass, String fieldName) {
        if (isProductionMode() || isProfileMode()) {
            return super.getPageField(pageClass, fieldName);
        }
        return (Field) getPageFields(pageClass).get(fieldName);
    }

    public Field[] getPageFieldArray(Class pageClass) {
        if (isProductionMode() || isProfileMode()) {
            return super.getPageFieldArray(pageClass);
        }
        return pageClass.getFields();
    }

    public Map getPageFields(Class pageClass) {
        if (isProductionMode() || isProfileMode()) {
            return super.getPageFields(pageClass);
        }
        Field[] fieldArray = getPageFieldArray(pageClass);
        Map fields = new HashMap();
        for (int i = 0; i < fieldArray.length; i++) {
            Field field = fieldArray[i];
            fields.put(field.getName(), field);
        }
        return fields;
    }

    static class PageMetaData {

        final Map headers;

        final String pageClassName;

        final String path;

        public PageMetaData(Element element, String pagesPackage,
            Map commonHeaders)
            throws ClassNotFoundException {

            // Set headers
            Map aggregationMap = new HashMap(commonHeaders);
            Map pageHeaders = loadHeadersMap(element);
            aggregationMap.putAll(pageHeaders);
            headers = Collections.unmodifiableMap(aggregationMap);

            // Set path
            String pathValue = element.getAttribute("path");
            if (pathValue.charAt(0) != '/') {
                path = "/" + pathValue;
            } else {
                path = pathValue;
            }

            // Set pageClass
            String value = element.getAttribute("classname");
            if (value != null) {
                if (pagesPackage.trim().length() > 0) {
                    value = pagesPackage + "." + value;
                }
            } else {
                String msg = "No classname defined for page path " + path;
                throw new RuntimeException(msg);
            }

            pageClassName = value;
        }

        public Map getHeaders() {
            return headers;
        }

        public String getPageClassName() {
            return pageClassName;
        }

        public String getPath() {
            return path;
        }
    }

    class PathLookupAlgorithm {

        public String getPagePath(Class pageClass) {
            return constructPathFromClass(pageClass);
        }

        public String constructPathFromClass(Class pageClass) {
            String className = ClassUtils.getShortClassName(pageClass);
            String pageDir = calcPageDir(pageClass);
            String result = null;

            result = constructPathFromClass(pageDir, className);

            if (result != null) {
                return result;
            }

            //Not found? Try with/without 'Page'
            if (className.endsWith("Page")) {
                //Chop off the 'Page' string and try again
                String noPageClassName = className.substring(0, className.lastIndexOf("Page"));
                result = constructPathFromClass(pageDir, noPageClassName);
            } else {
                //Append the 'Page' string and try again
                String pageClassName = className + "Page";
                result = constructPathFromClass(pageDir, pageClassName);
            }

            return result;
        }

        /**
         * This method strips the pagesPackage from the class package.
         * Thus if clickApp.pagesPackage is 'com.mycorp', then the packageName
         * 'com.mycorp.contacts' becomes 'contacts'
         */
        public String calcPageDir(Class clazz) {
            int indexOfClassName = clazz.getName().lastIndexOf('.');
            String pageDir = "/";

            //If the clazz does not have a package return the root path
            if (indexOfClassName < 0) {
                return pageDir;
            }

            //Note the addition of the '.' after the package name below. This
            //ensures to check for a legal package instead of a false positive
            //like 'com.mycorp.con' which would also have qualified if the
            //package name was 'com.mycorp.contacts'.

            //The '+ 1' in the substring argument ensures the packageName
            //ends with '.' ie 'com.mycorp.'
            final String packageName = clazz.getName().substring(0,
                indexOfClassName + 1);

            //If the pagesPackage is not specified return the converted packageName
            if (getPagesPackage() == null || getPagesPackage().length() == 0) {
                return convertToAbsoluteDir(packageName);
            }

            //Also append a '.' at the end of the pagesPackage
            final String pagesPackage = getPagesPackage() + ".";

            //Check that pagesPackage is a substring of packageName
            if (packageName.startsWith(getPagesPackage())) {

                //Check that the pagesPackage and packageName is not equal
                if (packageName.length() != getPagesPackage().length()) {

                    //Subtract the pagesPackage from the specified class package
                    pageDir = packageName.substring(getPagesPackage().length() + 1);
                }
            } else {
                pageDir = packageName;
            }
            return convertToAbsoluteDir(pageDir);
        }

        public String getPagesPackage() {
            return pagesPackage;
        }
        
        /**
         * Prefix the name with a '/' and change any '.' to '/'
         */
        public String convertToAbsoluteDir(String packageName) {
            packageName = ensurePathStartsWithSlash(packageName);
            return packageName.replace('.', '/');
        }

        protected String constructPathFromClass(String pageDir,
            String className) {
            String result = _constructPathFromClass(pageDir, className + ".htm");
            if (result == null) {
                result = _constructPathFromClass(pageDir, className + ".jsp");
            }
            return result;
        }

        private String _constructPathFromClass(String pageDir, String className) {
            pageDir = ensurePathStartsWithSlash(pageDir);

            //The 'path from class' lookup strategy is as follows
            //1. do not change the classname, just do lookup
            String path = pageDir + className;
            URL resource = tryAndFindEntryForPath(path);
            if (resource != null) {
                return path;
            }

            //2. lower case the first character
            String lowercase = Character.toString(Character.toLowerCase(className.charAt(0)));
            path = pageDir + lowercase + className.substring(1);
            resource = tryAndFindEntryForPath(path);
            if (resource != null) {
                return path;
            }

            //3. normalize camel case class to path tokenized on '-'
            path = pageDir + camelCaseToPath(className, "-");
            resource = tryAndFindEntryForPath(path);
            if (resource != null) {
                return path;
            }

            //3. normalize camel case class to path tokenized on '-'
            path = pageDir + camelCaseToPath(className, "_");
            resource = tryAndFindEntryForPath(path);
            if (resource != null) {
                return path;
            }
            return null;
        }

        protected String camelCaseToPath(String camelString, String token) {
            HtmlStringBuffer buffer = new HtmlStringBuffer();
            char[] chars = camelString.toCharArray();
            int length = chars.length;

            //Append first char
            buffer.append(Character.toLowerCase(chars[0]));

            for (int i = 1; i < length; i++) {
                if (Character.isUpperCase(chars[i])) {
                    buffer.append(token);
                    buffer.append(Character.toLowerCase(chars[i]));
                } else {
                    buffer.append(chars[i]);
                }
            }
            return buffer.toString();
        }

        protected URL tryAndFindEntryForPath(String path) {
            try {
                //path = ensurePathStartsWithSlash(path);
                URL resource = getServletContext().getResource(path);
                return resource;
            } catch (Exception ex) {
            }
            return null;
        }

        protected String ensurePathStartsWithSlash(String path) {
            if (StringUtils.isBlank(path)) {
                return "/";
            }
            if (path.charAt(0) != '/') {
                return '/' + path;
            } else {
                return path;
            }
        }
    }
}

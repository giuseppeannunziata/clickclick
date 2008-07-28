/*
 * Copyright 2008 Bob Schellink
 *
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
package net.sf.clickclick.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import net.sf.click.Control;
import net.sf.click.Page;
import net.sf.click.service.LogService;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.Format;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.PageImports;
import org.apache.commons.lang.StringUtils;

/**
 * Provides a utility object for rendering a Page's HTML header imports and its
 * control HTML header imports.
 * <p/>
 * A <tt>PageImports</tt> instance is automatically added to the Velocity Context
 * for Velocity templates, or as a request attribute for JSP pages using the key
 * name "<span class="blue">imports</span>".
 * <p/>
 * To use the <tt>PageImports</tt> object simply reference it your page header
 * section. For example:
 * <pre class="codeHtml">
 * &lt;html&gt;
 *  &lt;head&gt;
 *   <span class="blue">$imports</span>
 *  &lt;/head&gt;
 *  &lt;body&gt;
 *   <span class="red">$form</span>
 *  &lt;body&gt;
 * &lt;/html&gt; </pre>
 *
 * "<span class="blue">imports</span>" include all javascript and stylesheet
 * imports.
 * <p/>
 * PageImports also provides a way of including the javascript and stylesheet
 * separately using the key names "<span class="blue">cssImports</span>" and
 * "<span class="blue">jsImports</span>".
 * <p/>
 * You should follow the performance best practice by importing CSS includes
 * in the head section, then include the JS imports after the html body.
 * For example:
 * <pre class="codeHtml">
 * &lt;html&gt;
 *  &lt;head&gt;
 *   <span class="blue">$cssImports</span>
 *  &lt;/head&gt;
 *  &lt;body&gt;
 *   <span class="red">$form</span>
 *   &lt;br/&gt;
 *   <span class="red">$table</span>
 *  &lt;body&gt;
 * &lt;/html&gt;
 * <span class="blue">$jsImports</span>
 * </pre>
 *
 * Please also see {@link Page#getHtmlImports()} and
 * {@link Control#getHtmlImports()}.
 *
 * @see Format
 *
 * @author Malcolm Edgar
 */
public class AdvancedPageImports extends PageImports {

    // -------------------------------------------------------- Variables

    /** The list of CSS scripts. */
    private List cssInclude = new ArrayList();

    /** The set of unique CSS imports and includes. */
    private Set cssUniqueSet = new HashSet();

    /** The global CSS include. */
    private CssInclude cssGlobalInclude;

    /** The global Javascript include. */
    private JavascriptInclude jsGlobalInclude;

    /** The set of unique Javascript imports and includes. */
    private Set jsUniqueSet = new HashSet();

    // -------------------------------------------------------- Public Constructors

    /**
     * Create a page control HTML includes object.
     *
     * @param page the page to provide HTML includes for
     */
    public AdvancedPageImports(Page page) {
        super(page);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Populate the specified model with html import keys.
     *
     * @param model the model to populate with html import keys
     */
    public void popuplateTemplateModel(Map model) {
        LogService logger = ClickUtils.getLogService();
        Object pop = model.put("imports", new Imports());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"imports\". The page model object "
                         + pop + " has been replaced with a PageImports object";
            logger.warn(msg);
        }

        pop = model.put("cssImports", new CssImports());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
            + " model contains an object keyed with reserved "
            + "name \"cssImports\". The page model object "
            + pop + " has been replaced with a PageImports object";
            logger.warn(msg);
        }

        pop = model.put("jsImports", new JsImports());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
            + " model contains an object keyed with reserved "
            + "name \"jsImports\". The page model object "
            + pop + " has been replaced with a PageImports object";
            logger.warn(msg);
        }

        pop = model.put("jsImportsHead", new JsImportsHead());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
            + " model contains an object keyed with reserved "
            + "name \"jsImportsHead\". The page model object "
            + pop + " has been replaced with a PageImports object";
            logger.warn(msg);
        }

        pop = model.put("jsImportsBody", new JsImportsBody());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
            + " model contains an object keyed with reserved "
            + "name \"jsImportsBody\". The page model object "
            + pop + " has been replaced with a PageImports object";
            logger.warn(msg);
        }
    }

    /**
     * Populate the specified request with html import keys.
     *
     * @param request the http request to populate
     * @param model the model to populate with html import keys
     */
    public void popuplateRequest(HttpServletRequest request, Map model) {
        LogService logger = ClickUtils.getLogService();
        request.setAttribute("imports", new Imports());
        if (model.containsKey("imports")) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                             + " model contains an object keyed with reserved "
                             + "name \"imports\". The request attribute "
                             + "has been replaced with a PageImports object";
            logger.warn(msg);
        }

        request.setAttribute("cssImports", new CssImports());
        if (model.containsKey("cssImports")) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                             + " model contains an object keyed with reserved "
                             + "name \"cssImports\". The request attribute "
                             + "has been replaced with a PageImports object";
            logger.warn(msg);
        }

        request.setAttribute("jsImports", new JsImports());
        if (model.containsKey("jsImports")) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                             + " model contains an object keyed with reserved "
                             + "name \"jsImports\". The request attribute "
                             + "has been replaced with a PageImports object";
            logger.warn(msg);
        }

        request.setAttribute("jsImportsHead", new JsImportsHead());
        if (model.containsKey("jsImportsHead")) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                             + " model contains an object keyed with reserved "
                             + "name \"jsImportsHead\". The request attribute "
                             + "has been replaced with a PageImports object";
            logger.warn(msg);
        }

        request.setAttribute("jsImportsBody", new JsImportsBody());
        if (model.containsKey("jsImportsBody")) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                             + " model contains an object keyed with reserved "
                             + "name \"jsImportsBody\". The request attribute "
                             + "has been replaced with a PageImports object";
            logger.warn(msg);
        }
    }

    public void add(CssImport cssImport) {
        if (cssUniqueSet.contains(cssImport.getHref())) {
            // Already contain this import source
            return;
        }
        cssUniqueSet.add(cssImport.getHref());
        cssImports.add(cssImport);
    }

    public void add(CssInclude cssInclude) {
        if (cssInclude.isUnique()) {
            if (cssUniqueSet.contains(cssInclude.getInclude().toString())) {
                // Already contain this css include
                return;
            }
            cssUniqueSet.add(cssInclude.getInclude().toString());
        }
        this.cssInclude.add(cssInclude);
    }

    public void add(JavascriptImport javascriptImport) {
        if (jsUniqueSet.contains(javascriptImport.getSource())) {
            // Already contain this import source
            return;
        }
        jsUniqueSet.add(javascriptImport.getSource());
        jsImports.add(javascriptImport);
    }

    public void add(JavascriptInclude javascriptInclude) {
        if (javascriptInclude.isUnique()) {
            if (jsUniqueSet.contains(javascriptInclude.getInclude().toString())) {
                // Already contain this JavaScript include
                return;
            }
            jsUniqueSet.add(javascriptInclude.getInclude().toString());
        }
        jsScripts.add(javascriptInclude);
    }

    public void appendToGlobalScript(String script) {
        getGlobalScript().append(script);
    }

    public JavascriptInclude getGlobalScript() {
        if (jsGlobalInclude == null) {
            jsGlobalInclude = new JavascriptInclude();
        }
        return jsGlobalInclude;
    }

    public void appendToGlobalStyle(String style) {
        getGlobalStyle().append(style);
    }

    public CssInclude getGlobalStyle() {
        if (cssGlobalInclude == null) {
            cssGlobalInclude = new CssInclude();
        }
        return cssGlobalInclude;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Process the Page's set of control HTML head imports.
     */
    protected void processPageControls() {
        if (initialize) {
            return;
        }

        initialize = true;

        if (page.hasControls()) {
            for (int i = 0; i < page.getControls().size(); i++) {
                Control control = (Control) page.getControls().get(i);

                processLine(control.getHtmlImports());
            }
        }

        processLine(page.getHtmlImports());
    }

    /**
     * Process the given control HTML import line.
     *
     * @param value the HTML import line to process
     */
    protected void processLine(String value) {
        if (value == null || value.length() == 0) {
            return;
        }

        String[] lines = StringUtils.split(value, '\n');

        for (int i = 0; i  < lines.length; i++) {
            String line = lines[i].trim().toLowerCase();
            if (line.startsWith("<link") && line.indexOf("text/css") != -1) {
                CssImport cssImport = asCssImport(lines[i]);
                add(cssImport);

            } else if (line.startsWith("<style") && line.indexOf("text/css") != -1) {
                CssInclude cssInclude = asCssInclude(lines[i]);
                cssInclude.setUnique(true);
                add(cssInclude);

            } else if (line.startsWith("<script")) {
                if (line.indexOf(" src=") != -1) {
                    JavascriptImport javascriptImport = asJavascriptImport(lines[i]);
                    add(javascriptImport);

                } else {
                    JavascriptInclude javascriptInclude = asJavascriptInclude(lines[i]);
                    javascriptInclude.setUnique(true);
                    add(javascriptInclude);

                }
            } else {
                throw new IllegalArgumentException("Unknown include type: " + lines[i]);
            }
        }
    }

    /**
     * Add the given string item to the list if it is not already present.
     *
     * @param item the line item to add
     * @param list the list to add the item to
     */
    protected void addToList(String item, List list) {
        item = item.trim();

        boolean found = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(item)) {
                found = true;
                break;
            }
        }

        if (!found) {
            list.add(item);
        }
    }

    /**
     * Return a HTML string of all the page's HTML imports, including:
     * CSS imports, CSS includes, JS imports and JS includes.
     *
     * @return a HTML string of all the page's HTML imports, including:
     * CSS imports, CSS includes, JS imports and JS includes.
     */
    protected String getAllIncludes() {
        processPageControls();

        HtmlStringBuffer buffer = new HtmlStringBuffer(
              80 * jsImports.size()
            + 80 * jsScripts.size()
            + 80 * cssImports.size()
            + 80 * cssInclude.size());

        buffer.append(getCssImports());
        buffer.append(getJsImports());

        return buffer.toString();
    }

    /**
     * Return a HTML string of all the page's HTML CSS
     * {@link #cssImports imports}, {@link #cssInclude scripts} and
     * {@link #cssGlobalInclude}.
     *
     * @return only css imports and scripts
     */
    protected String getCssImports() {
        processPageControls();

        HtmlStringBuffer buffer = new HtmlStringBuffer(
            80 * cssImports.size() + 80 * cssInclude.size());

        // First include all the imports e.g. <link href="...">
        for (Iterator it = cssImports.iterator(); it.hasNext();) {
            CssImport cssImport = (CssImport) it.next();
            buffer.append(cssImport.toString());
            buffer.append('\n');
        }

        // Then include all the styles e.g. <style>...</style>
        for (Iterator it = cssInclude.iterator(); it.hasNext();) {
            CssInclude cssInclude = (CssInclude) it.next();
            buffer.append(cssInclude.toString());
            buffer.append('\n');
        }

        // Lastly include the global css include
        if (cssGlobalInclude != null) {
            buffer.append(cssGlobalInclude.toString());
        }

        return buffer.toString();
    }

    /**
     * Return a HTML string of all the page's HTML JS imports and scripts.
     * <p/>
     * This includes {@link #jsImports}, {@link #jsScripts} and
     * {@link #jsGlobalInclude}.
     *
     * @return all javascript imports and scripts
     */
    protected String getJsImports() {
        processPageControls();

        HtmlStringBuffer buffer = new HtmlStringBuffer(
              80 * jsImports.size() + 80 * jsScripts.size());

        // First include all the imports e.g. <script src="...">
        for (Iterator it = jsImports.iterator(); it.hasNext();) {
            JavascriptImport javascriptImport = (JavascriptImport) it.next();
            buffer.append(javascriptImport.toString());
            buffer.append('\n');
        }

        // Then include all the scripts e.g. <script>...</script>
        for (Iterator it = jsScripts.iterator(); it.hasNext();) {
            JavascriptInclude javascriptInclude = (JavascriptInclude) it.next();
            buffer.append(javascriptInclude.toString());
            buffer.append('\n');
        }

        // Lastly include global javascript
        if (jsGlobalInclude != null) {
            buffer.append(jsGlobalInclude.toString());
        }

        return buffer.toString();
    }

    /**
     * Return a string containing javascript imports and includes which should
     * be included in the head of the Page. This includes {@link #jsImports},
     * {@link #jsScripts} and {@link #jsGlobalInclude}.
     *
     * @return all head javascript imports and scripts
     */
    protected String getJsImportsHead() {
        processPageControls();

        HtmlStringBuffer buffer = new HtmlStringBuffer(
            80 * jsImports.size() + 80 * jsScripts.size());

        // First include all the imports e.g. <script src="...">
        for (Iterator it = jsImports.iterator(); it.hasNext();) {
            JavascriptImport javascriptImport = (JavascriptImport) it.next();
            if (javascriptImport.getPosition() == JavascriptImport.HEAD) {
                buffer.append(javascriptImport.toString());
                buffer.append('\n');
            }
        }

        // Then include all the scripts e.g. <script>...</script>
        for (Iterator it = jsScripts.iterator(); it.hasNext();) {
            JavascriptImport javascriptInclude = (JavascriptImport) it.next();
            if (javascriptInclude.getPosition() == JavascriptImport.HEAD) {
                buffer.append(javascriptInclude.toString());
                buffer.append('\n');
            }
        }

        // Lastly include global javascript if targeted for head of the Page
        if (jsGlobalInclude != null) {
            if (jsGlobalInclude.getPosition() == JavascriptInclude.HEAD) {
                buffer.append(jsGlobalInclude.toString());
            }
        }

        return buffer.toString();
    }

    /**
     * Return a string containing javascript imports and includes which should
     * be included in the body of the Page. This includes {@link #jsImports},
     * {@link #jsScripts} and {@link #jsGlobalInclude}.
     *
     * @return all body javascript imports and scripts
     */
    protected String getJsImportsBody() {
        processPageControls();

        HtmlStringBuffer buffer = new HtmlStringBuffer(
            80 * jsImports.size() + 80 * jsScripts.size());

        // First include all the body imports e.g. <script src="...">
        for (Iterator it = jsImports.iterator(); it.hasNext();) {
            JavascriptImport javascriptImport = (JavascriptImport) it.next();
            if (javascriptImport.getPosition() == JavascriptImport.BODY) {
                buffer.append(javascriptImport.toString());
                buffer.append('\n');
            }
        }

        
        // Then include all the scripts e.g. <script>...</script>
        for (Iterator it = jsScripts.iterator(); it.hasNext();) {
            JavascriptImport javascriptInclude = (JavascriptImport) it.next();
            if (javascriptInclude.getPosition() == JavascriptImport.BODY) {
                buffer.append(javascriptInclude.toString());
                buffer.append('\n');
            }
        }

        // Lastly include global javascript if targeted for body of the Page
        if (jsGlobalInclude != null) {
            if (jsGlobalInclude.getPosition() == JavascriptInclude.BODY) {
                buffer.append(jsGlobalInclude.toString());
            }
        }

        return buffer.toString();
    }

    // -------------------------------------------------------- Internal Classes

    /**
     * This class enables lazy, on demand importing for {@link #getAllIncludes()}.
     */
    class Imports {
        public String toString() {
            return AdvancedPageImports.this.getAllIncludes();
        }
    }

    /**
     * This class enables lazy, on demand importing for {@link #getJavascriptImports()}.
     */
    class JsImports {
        public String toString() {
            return AdvancedPageImports.this.getJsImports();
        }
    }

    /**
     * This class enables lazy, on demand importing for {@link #getJavascriptImportsHead()}.
     */
    class JsImportsHead {
        public String toString() {
            return AdvancedPageImports.this.getJsImportsHead();
        }
    }

    /**
     * This class enables lazy, on demand importing for {@link #getJavascriptImportsBody()}.
     */
    class JsImportsBody {
        public String toString() {
            return AdvancedPageImports.this.getJsImportsBody();
        }
    }

    /**
     * This class enables lazy, on demand importing for {@link #getCssImports()}.
     */
    class CssImports {
        public String toString() {
            return AdvancedPageImports.this.getCssImports();
        }
    }

    // -------------------------------------------------------- Private Methods

    private CssImport asCssImport(String line) {
        CssImport cssImport = new CssImport();
        setAttributes(cssImport, line);
        return cssImport;
    }

    private CssInclude asCssInclude(String line) {
        CssInclude cssInclude = new CssInclude();
        setAttributes(cssInclude, line);
        cssInclude.append(extractContent(line));
        return cssInclude;
    }

    private JavascriptImport asJavascriptImport(String line) {
        JavascriptImport javascriptInclude = new JavascriptImport();
        setAttributes(javascriptInclude, line);
        return javascriptInclude;
    }

    private JavascriptInclude asJavascriptInclude(String line) {
        JavascriptInclude javascriptInclude = new JavascriptInclude();
        setAttributes(javascriptInclude, line);
        javascriptInclude.append(extractContent(line));
        return javascriptInclude;
    }

    private String extractContent(String line) {
        if (line.endsWith("/>")) {
            // If tag has no content, exit early
            return "";
        }

        // Find index where tag ends
        int start = line.indexOf('>');
        if (start == -1) {
            throw new IllegalArgumentException(line + " is not a valid element");
        }
        int end = line.indexOf('<', start);
        if (end == -1) {
            return "";
        }
        return line.substring(start + 1, end);
    }

    /**
     * input -> style="moo:ok;param:value" hello="ok"
     * @param line
     * @return
     */
    private void setAttributes(AbstractResource control, String line) {
        // Find index where attributes start -> first space char
        int start = line.indexOf(' ');
        if (start == -1) {
            // If no attributes found, exit early
            return;
        }

        // Find index where attributes end -> closing tag
        int end = line.indexOf("/>");
        if (end == -1) {
            end = line.indexOf(">");
        }
        if (end == -1) {
            throw new IllegalArgumentException(line + " is not a valid css import");
        }

        line = line.substring(start, end);
        StringTokenizer tokens = new StringTokenizer(line, " ");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            StringTokenizer attribute = new StringTokenizer(token, "=");
            String key = attribute.nextToken();
            String value = attribute.nextToken();
            control.setAttribute(key, StringUtils.strip(value, "'\""));
        }
    }
}

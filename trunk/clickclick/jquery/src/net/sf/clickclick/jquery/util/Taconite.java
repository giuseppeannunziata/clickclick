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
package net.sf.clickclick.jquery.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.Control;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.util.Partial;
import org.apache.click.MockContext;
import org.apache.click.element.CssStyle;
import org.apache.click.element.Element;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.DateField;
import org.apache.click.util.PageImports;
import org.apache.commons.lang.StringUtils;

/**
 * Provides a Java API which wraps the JQuery Taconite plugin:
 * http://www.malsup.com/jquery/taconite
 * <p/>
 * The API supports all Taconite commands:
 * http://www.malsup.com/jquery/taconite/#commands
 * <p/>
 * You can even dynamically append scripts to the page &lt;head&gt; section:
 * <pre class="prettyprint">
 * <append select="head">
 *     <script type="text/javascript">
 *         // wire up the 'wireMe' button on the fly
 *         $('#wireMe').click(function() {
 *             alert('Button clicked!');
 *         }).val("Wired!");
 *     </script>
 * </append>
 * </pre>
 *
 * @author Bob Schellink
 */
public class Taconite extends Partial {

    // --------------------------------------------------------------- Commands

    public static final String APPEND = "append";
    
    public static final String PREPEND = "prepend";
    
    public static final String AFTER = "after";
    
    public static final String BEFORE = "before";
    
    public static final String REPLACE = "replace";

    public static final String REPLACE_CONTENT = "replaceContent";

    public static final String ATTR = "attr";

    public static final String WRAP = "wrap";

    public static final String HIDE = "hide";

    public static final String EVAL = "eval";
    
    public static final String REMOVE = "remove";
    
    public static final String EMPTY = "empty";

    static final String HEAD_ELEMENTS = "addHeader";

    private List commands = new ArrayList();

    public Taconite() {
        setContentType(XML);
    }

    public Taconite(Command command) {
        setContentType(XML);
        add(command);
    }

    public String getTag() {
        return "taconite";
    }

    public Taconite add(Command command) {
        commands.add(command);
        return this;
    }

    public Taconite remove(Command command) {
        commands.remove(command);
        return this;
    }

    public Command append(String select, String content) {
        return add(Taconite.APPEND, select, content);
    }

    public Command append(String select, Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.APPEND, select, control);
    }

    public Command append(Control control, String content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.APPEND, getSelector(control), content);
    }

    public Command append(Control control, Control content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.APPEND, getSelector(control), content);
    }

    public Command prepend(String select, String content) {
        return add(Taconite.PREPEND, select, content);
    }

    public Command prepend(String select, Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.PREPEND, select, control);
    }

    public Command prepend(Control control, String content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.PREPEND, getSelector(control), content);
    }

    public Command prepend(Control control, Control content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.PREPEND, getSelector(control), content);
    }

    public Command after(String select, String content) {
        return add(Taconite.AFTER, select, content);
    }

    public Command after(String select, Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.AFTER, select, control);
    }

    public Command after(Control control, String content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.AFTER, getSelector(control), content);
    }

    public Command after(Control control, Control content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.AFTER, getSelector(control), content);
    }

    public Command before(String select, String content) {
        return add(Taconite.BEFORE, select, content);
    }

    public Command before(String select, Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.BEFORE, select, control);
    }

    public Command before(Control control, String content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.BEFORE, getSelector(control), content);
    }

    public Command before(Control control, Control content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.BEFORE, getSelector(control), content);
    }

    public Command remove(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REMOVE, getSelector(control));
    }

    public Command remove(String select) {
        return add(Taconite.REMOVE, select);
    }

    public Command replace(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE, getSelector(control), control);
    }

    public Command replace(String select, Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE, select, control);
    }

    public Command replace(String select, String content) {
        return add(Taconite.REPLACE, select, content);
    }

    public Command replace(String select, Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null");
        }
        return add(Taconite.REPLACE, select, element);
    }

    public Command replaceContent(String select, String content) {
        return add(Taconite.REPLACE_CONTENT, select, content);
    }

    public Command replaceContent(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, getSelector(control), control);
    }

    public Command replaceContent(Control control, Control content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, getSelector(control), content);
    }

    public Command replaceContent(Control control, String content) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, getSelector(control), content);
    }

    public Command replaceContent(String select, Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, select, control);
    }

    public Command replaceContent(String select, Element element) {
        return add(Taconite.REPLACE_CONTENT, select, element);
    }

    public Command eval(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }
        Command command = createCommand(Taconite.EVAL);
        command.getContent().add(content);
        return command;
    }

    public Command empty(Control control) {
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.EMPTY, getSelector(control));
    }

    public Command empty(String select) {
        if (select == null) {
            throw new IllegalArgumentException("Select is null");
        }
        return add(Taconite.EMPTY, select);
    }

    public Command add(String commandName, String select) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        Command command = createCommand(commandName, select);
        return command;
    }

    public Command add(String commandName, String select, Control control) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (control == null) {
            throw new IllegalArgumentException("Control is null");
        }
        Command command = createCommand(commandName, select);
        command.add(control);
        return command;
    }

    public Command add(String commandName, String select, String content) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null or blank: " + content);
        }
        Command command = createCommand(commandName, select);
        command.add(content);
        return command;
    }

    public Command add(String commandName, String select, Element element) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (element == null) {
            throw new IllegalArgumentException("Element is null");
        }
        Command command = createCommand(commandName, select);
        command.add(element);
        return command;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) {
        setReader(new StringReader(toString()));
        super.render(request, response);
    }

    public void render(HtmlStringBuffer buffer) {
        processHeadElements();

        String tag = getTag();
        renderTagBegin(tag, buffer);
        buffer.closeTag();
        buffer.append("\n");

        renderContent(buffer);

        renderTagEnd(tag, buffer);
    }

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(commands.size() + 150);
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    protected String getSelector(Control control) {
        String selector = AjaxUtils.getSelector(control);
        if (selector == null) {
            throw new IllegalArgumentException("No selector could be found for"
                + " the control: " + control.getClass().getName() + "#"
                + control.getName());
        }
        return selector;
    }

    protected void renderTagBegin(String tagName, HtmlStringBuffer buffer) {
        buffer.elementStart(tagName);
    }

    protected void renderContent(HtmlStringBuffer buffer) {
        Iterator it = commands.iterator();
        while(it.hasNext()) {
            Command command = (Command) it.next();
            command.render(buffer);
            buffer.append("\n");
        }
    }

    protected void renderTagEnd(String tagName, HtmlStringBuffer buffer) {
        buffer.elementEnd(tagName);
    }

    protected void processHeadElements() {
        if (commands.size() == 0) {
            return;
        }

        PageImports pageImports = new PageImports(null);

        Iterator it = commands.iterator();
        while(it.hasNext()) {
            Command command = (Command) it.next();
            processHeadElements(command, pageImports);
        }

        List headElements = pageImports.getHeadElements();
        List jsElements = pageImports.getJsElements();

        Command headElementsCommand = new Command(Taconite.HEAD_ELEMENTS);
        headElementsCommand.setContent(headElements);

        // Place all JsScripts at the bottom of the command list which ensures
        // that new HTML elements added through Taconite are present in the DOM
        // when scripts are executed. Otherwise scripts which target elements
        // will fail as the elements are only loaded after the script executes
        Command jsScriptsCommand = new Command(Taconite.HEAD_ELEMENTS);
        addJavaScriptElements(headElementsCommand, jsScriptsCommand, jsElements);

        // Add headElements at the top of the command list
        if(headElementsCommand.getContent().size() > 0) {
            commands.add(0, headElementsCommand);
        }

        // Add JsScript elements at the bottom of the command list
        if(jsScriptsCommand.getContent().size() > 0) {
            commands.add(jsScriptsCommand);
        }
    }

    protected Command createCommand(String commandName) {
        Command command = new Command(commandName);
        add(command);
        return command;
    }

    protected Command createCommand(String commandName, String select) {
        Command command = new Command(commandName);
        if (StringUtils.isNotBlank(select)) {
            command.setSelect(select);
        }
        add(command);
        return command;
    }

    // ------------------------------------------------ Package Private Methods

    void processHeadElements(Command command, PageImports pageImports) {
        Iterator it = command.getContent().iterator();
        while(it.hasNext()) {
            Object content = it.next();
            if (content instanceof Control) {
                Control control = (Control) content;

                // Ensure the deprecated HtmlImports are included.
                pageImports.addImport(control.getHtmlImports());

                // Ensure the head elements are included
                pageImports.processControl(control);
            }
        }
    }

    void addJavaScriptElements(Command headElementsCommand, Command jsScriptsCommand,
        List jsElements) {

        Iterator it = jsElements.iterator();

        while(it.hasNext()) {
            Element element = (Element) it.next();
            if (element instanceof JsScript) {
                JsScript jsScript = (JsScript) element;
                jsScript.setCharacterData(true);

                // If any JavaScript relies on a DOM ready function, we need to
                // ensure this function is *not* rendered because Ajax requests
                // does not trigger DOM ready functions.
                if (jsScript.isExecuteOnDomReady()) {
                    jsScript.setExecuteOnDomReady(false);

                    // Nullify the ID attribute otherwise the script won't be
                    // included in the Page by the jquery.click.js script
                    jsScript.setId(null);
                }
                jsScriptsCommand.add(element);

            } else if (element instanceof CssStyle) {
                ((CssStyle) element).setCharacterData(true);
                headElementsCommand.add(element);
            } else {
                headElementsCommand.add(element);
            }
        }
    }

    /*
    JsScript createDomReadyScript() {
        JsScript script = new JsScript();
        script.setId("set-dom-ready");
        script.append("if (typeof(Click) != 'undefined')");
        script.append("if (typeof(Click.domready) != 'undefined')");
        script.append("Click.domready.ready = true;");
        script.setCharacterData(true);
        return script;
    }*/

    public static void main(String[] args) {
        MockContext.initContext();
        Taconite partial = new Taconite();
        partial.replace(new DateField("date"));
        System.out.println(partial);
    }
}

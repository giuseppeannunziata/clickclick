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
 * Provides a Java API for the JQuery Taconite plugin:
 * <a href="http://www.malsup.com/jquery/taconite">http://www.malsup.com/jquery/taconite</a>.
 * <p/>
 * Taconite allows you to perform multiple DOM updates for a single Ajax request.
 * Taconite produces an XML document consisting of commands that is executed
 * in order by the browser.
 * <p/>
 * Common usages include: updating Controls, adding new Controls, removing Controls,
 * replace content of Controls and even executing raw JavaScript code.
 * <p/>
 * The API consists of two classes: Taconite and {@link Command} although in
 * most cases you will only use the Taconite class.
 * <p/>
 * This API provides a number of pre-defined commands for example:
 * {@link #REPLACE}, {@link #REPLACE_CONTENT}, {@link #AFTER}, {@link #EVAL}.
 * Do note that you are not limited to the predefined commands and can use any
 * other JQuery functions available, even your own custom defined functions.
 *
 * <h3>Example usage</h3>
 *
 * The example below demonstrates how to replace the content of a Span target
 * with today's Date. A Taconite command "<tt>replaceContent</tt>" is used to
 * replace the content of the Span.
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     private JQActionLink link = new JQActionLink("update", "Update Text", "updateId");
 *     private Span span = new Span("span", "spanId");
 *
 *     public void onInit() {
 *
 *         addControl(link);
 *         addControl(span);
 *
 *         link.setActionListener(new AjaxAdapter() {
 *
 *             // Implement the onAjaxAction method which receives Ajax requests
 *             public Partial onAjaxAction(Control source) {
 *                 // Note Taconite is a Partial subclass
 *                 Taconite partial = new Taconite();
 *
 *                 // Using a CSS selector to replace the Span content with the latest
 *                 // Date
 *                 partial.replaceContent(span, "Updated at " + new Date());
 *
 *                 return partial;
 *             }
 *         });
 *     }
 * } </pre>
 *
 * <h3>Multiple Commands</h3>
 *
 * You can add an unlimited amount of commands to the Taconite instance. For
 * example:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     ...
 *
 *     public void onInit() {
 *
 *         link.setActionListener(new AjaxAdapter() {
 *
 *             // Implement the onAjaxAction method which receives Ajax requests
 *             public Partial onAjaxAction(Control source) {
 *                 // Note Taconite is a Partial subclass
 *                 Taconite partial = new Taconite();
 *
 *                 // Using a CSS selector to replace the Span content with the latest
 *                 // Date
 *                 partial.replaceContent(span, "Updated at " + new Date());
 *
 *                 // Add a Form after the span
 *                 partial.after(span, form);
 *
 *                 // Add a Table after the Form
 *                 partial.after(form, table);
 *
 *                 return partial;
 *             }
 *         });
 *     }
 * } </pre>
 *
 * The commands are executed one after the other in the order they were added.
 * Note the last two Commands above where first the Form is added and then the
 * Table is added after the Form. This works because the Form has been added
 * by the time the Table is added.
 * <p/>
 * <b>Please note:</b> because Click Pages are <tt>stateless</tt> by default,
 * any Controls added to the Page via Ajax will be removed when the Page is
 * refreshed. It is therefore recommended to consider <tt>stateful</tt> Pages
 * when working with highly dynamic Pages.
 *
 * <h3>Creating custom commands</h3>
 *
 * It is very easy to create your own custom commands. See
 * <a href="Command.html#custom-commands">custom commands</a> for more details.
 *
 * <h3>JavaScript and CSS resource handling</h3>
 *
 * Taconite uses the JavaScript, "<tt>/clickclick/jquery/jquery.click.js</tt>",
 * to enable smooth integration with Apache Click.
 * <p/>
 * The "<tt>jquery.click.js</tt>" library includes the following JQuery plugins:
 * <ol>
 * <li><a href="http://www.malsup.com/jquery/taconite/">JQuery Taconite</a> - provides
 * ability to execute a list of commands in the browser</li>
 * <li><a href="http://docs.jquery.com/Plugins/livequery">JQuery LiveQuery</a> - allows
 * JavaScript events (onclick, onblur etc) that are bound on Controls
 * to persist even if the underlying Control is replaced by an Ajax request. The
 * latest versions of JQuery supports the new "<tt>live()</tt>" functionality
 * which is similar to LiveQuery, but it does not support all events yet.</li>
 * <li>Click specific functionality - to allow a Control's resource dependencies
 * (JavaScript and CSS) to be added through Ajax requests.</li>
 * </ol>
 *
 * Its worth discussing how Control resources (JavaScript and CSS) are handled
 * by Taconite.
 * <p/>
 * Firstly, Taconite ensures that all duplicate JavaScript and CSS resoures are
 * removed before returning the response. For example if you return two DateFields,
 * Taconite will ensure that only one set of JavaScript and CSS resources are returned.
 * Duplicate resources are identified as follows:
 *
 * <ul>
 * <li>{@link org.apache.click.element.JsImport} and {@link org.apache.click.element.CssImport}
 * are unique based on their attributes "<tt>src</tt>" and "<tt>href</tt>"
 * respectively.
 * <li>{@link org.apache.click.element.JsScript} and
 * {@link org.apache.click.element.CssStyle} are unique based on their "<tt>ID</tt>"
 * attribute. Note, if the ID attribute is not set Taconite won't be able to
 * determine if the resource is a duplicate or not.
 * </li>
 * </ul>
 *
 * Secondly, all JavaScript and CSS resources returned to the browser are checked
 * whether they should be added to the Page or not. The identification procedure
 * is the same as above:
 *
 * <ul>
 * <li>if a {@link org.apache.click.element.JsImport} or {@link org.apache.click.element.CssImport}
 * is already part of the Page, it is not added again.</li>
 * <li>if a {@link org.apache.click.element.JsScript} or {@link org.apache.click.element.CssStyle}
 * has an ID attribute defined, the Page is checked for <tt>Script</tt> and <tt>Style</tt>
 * elements with the same ID. If an existing element is found the
 * <tt>Script</tt> or <tt>Style</tt> is not added to the Page. Note, sometimes
 * it is desirable to execute a Script regardless if it has executed before. In
 * those situations simply ignore the ID attribute.
 * </li>
 * </ul>
 *
 * @author Bob Schellink
 */
public class Taconite extends Partial {

    // -------------------------------------------------------------- Constants

    /** The "<tt>append</tt>" command constant. */
    public static final String APPEND = "append";

    /** The "<tt>prepend</tt>" command constant. */
    public static final String PREPEND = "prepend";

    /** The "<tt>after</tt>" command constant. */
    public static final String AFTER = "after";

    /** The "<tt>before</tt>" command constant. */
    public static final String BEFORE = "before";

    /** The "<tt>after</tt>" command constant. */
    public static final String REPLACE = "replace";

    /** The "<tt>replaceContent</tt>" command constant. */
    public static final String REPLACE_CONTENT = "replaceContent";

    /** The "<tt>attr</tt>" command constant. */
    public static final String ATTR = "attr";

    /** The "<tt>wrap</tt>" command constant. */
    public static final String WRAP = "wrap";

    /** The "<tt>hide</tt>" command constant. */
    public static final String HIDE = "hide";

    /** The "<tt>show</tt>" command constant. */
    public static final String SHOW = "show";

    /**
     * The "<tt>eval</tt>" command constant for executed raw JavaScript in the
     * browser.
     */
    public static final String EVAL = "eval";

    /** The "<tt>remove</tt>" command constant. */
    public static final String REMOVE = "remove";

    /** The "<tt>empty</tt>" command constant. */
    public static final String EMPTY = "empty";

    /**
     * The "<tt>ADD_HEADER</tt>" command constant for adding JavaScript and CSS
     * resources to the Ajax response.
     */
    public static final String ADD_HEADER = "addHeader";

    // -------------------------------------------------------------- Variables

    /** The list of commands to execute. */
    private List commands = new ArrayList();

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default Taconite instance.
     * <p/>
     * Please note, the {@link #setContentType(java.lang.String)} is automatically
     * set to {@link #XML} since Taconite returns an XML document.
     */
    public Taconite() {
        setContentType(XML);
    }

    /**
     * Create a Taconite instance with the given command.
     * <p/>
     * Please note, the {@link #setContentType(java.lang.String)} is automatically
     * set to {@link #XML} since Taconite returns an XML document.
     *
     * @param command the command for this Taconite instance
     */
    public Taconite(Command command) {
        setContentType(XML);
        add(command);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the Taconite XML document root tag: "<tt>taconite</tt>".
     *
     * @return the the Taconite XML document root tag
     */
    public String getTag() {
        return "taconite";
    }

    /**
     * Add the given command to the list of commands.
     *
     * @param command the command to add
     * @return the Taconite instance
     */
    public Taconite add(Command command) {
        commands.add(command);
        return this;
    }

    /**
     * Remove the command from the list of commands.
     *
     * @param command the command to remove
     * @return the Taconite instance
     */
    public Taconite remove(Command command) {
        commands.remove(command);
        return this;
    }

    /**
     * Create an {@link #ADD_HEADER} command for the given Element and add it to
     * the list of commands.
     * <p/>
     * If the Element is an instance of {@link org.apache.click.element.JsImport},
     * {@link org.apache.click.element.CssImport} or
     * {@link org.apache.click.element.CssStyle} it will be added to the Page
     * HEAD (&lt;head&gt;).
     * <p/>
     * If the given Element is an instance of {@link org.apache.click.element.JsScript}
     * it will be evaluated instead.
     *
     * @param element the Element to add
     * @return the command that was created
     */
    public Command addHeader(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is null");
        }
        Command command = createCommand(Taconite.ADD_HEADER);
        command.add(element);
        return command;
    }

    /**
     * Create an {@link #ADD_HEADER} command for the given list of Elements and
     * add it to the list of commands.
     * <p/>
     * If an Element in the list is an instance of {@link org.apache.click.element.JsImport},
     * {@link org.apache.click.element.CssImport} or
     * {@link org.apache.click.element.CssStyle} it will be added to the Page
     * HEAD (&lt;head&gt;).
     * <p/>
     * If an Element in the list is an instance of {@link org.apache.click.element.JsScript}
     * it will be evaluated instead.
     *
     * @param elements the list of Elements to add
     * @return the command that was created
     */
    public Command addHeader(List<Element> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements are null");
        }
        Command command = createCommand(Taconite.ADD_HEADER);
        command.setContent(elements);
        return command;
    }

    /**
     * Create an {@link #AFTER} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added after the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to add after the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public Command after(String selector, String content) {
        return add(Taconite.AFTER, selector, content);
    }

    /**
     * Create an {@link #AFTER} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be added after the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to add after the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public Command after(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.AFTER, selector, content);
    }

    /**
     * Create an {@link #AFTER} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added after the target Control.
     *
     * @param target the target Control
     * @param content the content to add after the target Control
     * @return the command that was created
     */
    public Command after(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.AFTER, getSelector(target), content);
    }

    /**
     * Create an {@link #AFTER} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be added after the target Control.
     *
     * @param target the target Control
     * @param content the content to add after the target Control
     * @return the command that was created
     */
    public Command after(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.AFTER, getSelector(target), content);
    }

    /**
     * Create an {@link #APPEND} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be appended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to append to the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public Command append(String selector, String content) {
        return add(Taconite.APPEND, selector, content);
    }

    /**
     * Create an {@link #APPEND} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be appended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to append to the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public Command append(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.APPEND, selector, content);
    }

    /**
     * Create an {@link #APPEND} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be appended to the target Control.
     *
     * @param target the target Control
     * @param content the content to append to the target Control
     * @return the command that was created
     */
    public Command append(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.APPEND, getSelector(target), content);
    }

    /**
     * Create an {@link #APPEND} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be appended to the target Control.
     *
     * @param target the target Control
     * @param content the content to append to the target Control
     * @return the command that was created
     */
    public Command append(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.APPEND, getSelector(target), content);
    }

    /**
     * Create an {@link #ATTR} command for the given CSS selector and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return the command that was created
     */
    public Command attr(String selector, String name, String value) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return add(Taconite.ATTR, selector, name, value);
    }

    /**
     * Create an {@link #ATTR} command for the target Control and name/value
     * pair and add it to the list of commands.
     * <p/>
     * Set the attribute name/value pair of the target Control.
     *
     * @param target the target Control which attribute to set
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return the command that was created
     */
    public Command attr(Control target, String name, String value) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.ATTR, getSelector(target), name, value);
    }

    /**
     * Create an {@link #ATTR} command for the given CSS selector and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the element/s targeted by the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public Command attr(String selector, String... attributes) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return add(Taconite.ATTR, selector, attributes);
    }

    /**
     * Create an {@link #ATTR} command for the target Control and attributes
     * array and add it to the list of commands.
     * <p/>
     * Set the attributes of the target Control.
     *
     * @param target the target Control which attributes to set
     * @param attributes the attributes to set
     * @return the command that was created
     */
    public Command attr(Control target, String... attributes) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.ATTR, getSelector(target), attributes);
    }

    /**
     * Create a {@link #BEFORE} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added before the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to add before the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public Command before(String selector, String content) {
        return add(Taconite.BEFORE, selector, content);
    }

    /**
     * Create a {@link #BEFORE} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be added before the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to add before the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public Command before(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.BEFORE, selector, content);
    }

    /**
     * Create a {@link #BEFORE} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be added before the target Control.
     *
     * @param target the target Control
     * @param content the content to add before the target Control
     * @return the command that was created
     */
    public Command before(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.BEFORE, getSelector(target), content);
    }

    /**
     * Create a {@link #BEFORE} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be added before the target Control.
     *
     * @param target the target Control
     * @param content the content to add before the target Control
     * @return the command that was created
     */
    public Command before(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.BEFORE, getSelector(target), content);
    }

    /**
     * Create an {@link #EMPTY} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control's child nodes will be removed.
     *
     * @param target the target Control which child nodes to remove
     * @return the command that was created
     */
    public Command empty(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.EMPTY, getSelector(target));
    }

    /**
     * Create an {@link #EMPTY} command for the given CSS selector and add
     * it to the list of commands.
     * <p/>
     * The child nodes of the element/s targeted by the CSS selector will be
     * removed.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public Command empty(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return add(Taconite.EMPTY, selector);
    }

    /**
     * Create a {@link #HIDE} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control to hide.
     *
     * @param target the target Control to hide
     * @return the command that was created
     */
    public Command hide(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.HIDE, getSelector(target));
    }

    /**
     * Create a {@link #HIDE} command for the given CSS selector and add
     * it to the list of commands.
     * <p/>
     * The element/s targeted by the CSS selector will be hidden.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public Command hide(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return add(Taconite.HIDE, selector);
    }

    /**
     * Create an {@link #EVAL} command for the given JavaScript content and add
     * it to the list of commands.
     * <p/>
     * The given JavaScript content will evaluated in the browser.
     *
     * @param content the JavaScript content to be evaluated in the browser
     * @return the command that was created
     */
    public Command eval(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }
        Command command = createCommand(Taconite.EVAL);
        command.getContent().add(content);
        return command;
    }

    /**
     * Create a {@link #PREPEND} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be prepended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to prepend to the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public Command prepend(String selector, String content) {
        return add(Taconite.PREPEND, selector, content);
    }

    /**
     * Create a {@link #PREPEND} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will be prepended to the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to prepend to the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public Command prepend(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.PREPEND, selector, content);
    }

    /**
     * Create a {@link #PREPEND} command for the given Control and content
     * and add it to the list of commands.
     * <p/>
     * The given content will be prepended to the target Control.
     *
     * @param target the target Control
     * @param content the content to prepend to the target Control
     * @return the command that was created
     */
    public Command prepend(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.PREPEND, getSelector(target), content);
    }

    /**
     * Create a {@link #PREPEND} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will be prepended to the target Control.
     *
     * @param target the target Control
     * @param content the content to prepend to the target Control
     * @return the command that was created
     */
    public Command prepend(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.PREPEND, getSelector(target), content);
    }

    /**
     * Create a {@link #REMOVE} command for the target Control and add it to the
     * list of commands.
     * <p/>
     * The target Control will be remove.
     *
     * @param target the target Control
     * @return the command that was created
     */
    public Command remove(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REMOVE, getSelector(target));
    }

    /**
     * Create a {@link #REMOVE} command for the CSS selector and add it to the
     * list of commands.
     * <p/>
     * The element/s targeted by the CSS selector will be removed.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public Command remove(String selector) {
        return add(Taconite.REMOVE, selector);
    }

    /**
     * Create a {@link #REPLACE} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will replace the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to replace the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public Command replace(String selector, String content) {
        return add(Taconite.REPLACE, selector, content);
    }

    /**
     * Create a {@link #REPLACE} command for the target Control and add it to the
     * list of commands.
     * <p/>
     * The target Control will be replace with itself.
     *
     * @param target the target Control to replace itself with
     * @return the command that was created
     */
    public Command replace(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE, getSelector(target), target);
    }

    /**
     * Create a {@link #REPLACE} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will replace the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to replace the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public Command replace(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE, selector, content);
    }

    /**
     * Create a {@link #REPLACE} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content will replace the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the target Control
     * @return the command that was created
     */
    public Command replace(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE, getSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will replace the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the target Control with
     * @return the command that was created
     */
    public Command replace(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE, getSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the given CSS selector and
     * content and add it to the list of commands.
     * <p/>
     * The given content will replace the content of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content to replace the content of the element/s
     * targeted by the CSS selector
     * @return the command that was created
     */
    public Command replaceContent(String selector, String content) {
        return add(Taconite.REPLACE_CONTENT, selector, content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control will replace its content.
     *
     * @param target the target Control to replace its content with
     * @return the command that was created
     */
    public Command replaceContent(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, getSelector(target), target);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the target Control and
     * content and add it to the list of commands.
     * <p/>
     * The content Control will replace the content of the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the content of the target Control
     * with
     * @return the command that was created
     */
    public Command replaceContent(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, getSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the target Control and
     * content and add it to the list of commands.
     * <p/>
     * The content will replace the content of the target Control.
     *
     * @param target the target Control
     * @param content the content to replace the content of the target Control
     * with
     * @return the command that was created
     */
    public Command replaceContent(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, getSelector(target), content);
    }

    /**
     * Create a {@link #REPLACE_CONTENT} command for the given CSS selector and
     * Control and add it to the list of commands.
     * <p/>
     * The given Control will replace the content of the element/s targeted by
     * the CSS selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control to replace the content of the element/s
     * targeted by the CSS selector
     * @return the command that was created
     */
    public Command replaceContent(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.REPLACE_CONTENT, selector, content);
    }

    /**
     * Create a {@link #SHOW} command for the target Control and add
     * it to the list of commands.
     * <p/>
     * The target Control to show.
     *
     * @param target the target Control to show
     * @return the command that was created
     */
    public Command show(Control target) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.SHOW, getSelector(target));
    }

    /**
     * Create a {@link #SHOW} command for the given CSS selector and add
     * it to the list of commands.
     * <p/>
     * The element/s targeted by the CSS selector will be shown.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @return the command that was created
     */
    public Command show(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Selector is null");
        }
        return add(Taconite.SHOW, selector);
    }

    /**
     * Create a {@link #WRAP} command for the given CSS selector and content
     * and add it to the list of commands.
     * <p/>
     * The given content will wrap the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the content that will wrap the element/s targeted by the
     * CSS selector
     * @return the command that was created
     */
    public Command wrap(String selector, String content) {
        return add(Taconite.WRAP, selector, content);
    }

    /**
     * Create a {@link #WRAP} command for the given CSS selector and Control
     * and add it to the list of commands.
     * <p/>
     * The given Control will wrap the element/s targeted by the CSS
     * selector.
     *
     * @param selector the CSS selector e.g: '#myId' or '.myClass'.
     * @param content the Control that will wrap the element/s targeted by
     * the CSS selector
     * @return the command that was created
     */
    public Command wrap(String selector, Control content) {
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.WRAP, selector, content);
    }

    /**
     * Create a {@link #WRAP} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content Control will wrap the target Control.
     *
     * @param target the target Control
     * @param content the content that will wrap the target Control
     * @return the command that was created
     */
    public Command wrap(Control target, Control content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.WRAP, getSelector(target), content);
    }

    /**
     * Create a {@link #WRAP} command for the target Control and content
     * and add it to the list of commands.
     * <p/>
     * The content that will wrap the target Control.
     *
     * @param target the target Control
     * @param content the content that will wrap the target Control
     * @return the command that was created
     */
    public Command wrap(Control target, String content) {
        if (target == null) {
            throw new IllegalArgumentException("Control is null");
        }
        return add(Taconite.WRAP, getSelector(target), content);
    }

    public Command add(String commandName, String selector) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        Command command = createCommand(commandName, selector);
        return command;
    }

    public Command add(String commandName, String selector, Control content) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (content == null) {
            throw new IllegalArgumentException("Control is null");
        }
        Command command = createCommand(commandName, selector);
        command.add(content);
        return command;
    }

    public Command add(String commandName, String selector, String content) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null or blank: " + content);
        }
        Command command = createCommand(commandName, selector);
        command.add(content);
        return command;
    }

    public Command add(String commandName, String selector, String name, String value) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        Command command = createCommand(commandName, selector);
        command.setName(name);
        command.setValue(value);
        return command;
    }

    public Command add(String commandName, String selector, String... attributes) {
        if (StringUtils.isBlank(commandName)) {
            throw new IllegalArgumentException("Command name is null or blank: " + commandName);
        }
        if (attributes == null) {
            throw new IllegalArgumentException("attributes is null");
        }
        Command command = createCommand(commandName, selector);
        command.arguments(attributes);
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

        Command headElementsCommand = new Command(Taconite.ADD_HEADER);
        headElementsCommand.setContent(headElements);

        // Place all JsScripts at the bottom of the command list which ensures
        // that new HTML elements added through Taconite are present in the DOM
        // when scripts are executed. Otherwise scripts which target elements
        // will fail as the elements are only loaded after the script executes
        Command jsScriptsCommand = new Command(Taconite.ADD_HEADER);
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
            command.setSelector(select);
        }
        add(command);
        return command;
    }

    // ------------------------------------------------ Package Private Methods

    void processHeadElements(Command command, PageImports pageImports) {
        Iterator it = command.getContent().iterator();
        while(it.hasNext()) {
            Object content = it.next();

            // Add all controls to the PageImports instance
            if (content instanceof Control) {
                Control control = (Control) content;

                // Ensure the deprecated HtmlImports are included.
                pageImports.addImport(control.getHtmlImports());

                // Ensure the head elements are included
                pageImports.processControl(control);
            } else if (content instanceof Element) {
                // Add all Elements to the PageImports instance

                pageImports.add((Element) content);
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

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
package net.sf.clickclick.jquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.Control;
import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides the Taconite Command class which is based on JQuery Taconite
 * <a href="http://malsup.com/jquery/taconite/#commands">commands</a>.
 * <p/>
 * Commands map onto JQuery functions and consist of the following parts:
 * <ul>
 * <li>{@link #command} - the command to execute, normally a JQuery function
 * e.g: append, prepend, remove etc.</li>
 * <li>{@link #selector} - a CSS selector to identify the target DOM elements.</li>
 * <li>{@link #content} (optional) - a list of payloads for the command, usually one or more
 * Click Controls but could be arbitrary HTML markup.</li>
 * <li>{@link #name} (optional) - the name of an attribute</li>
 * <li>{@link #value} (optional) - the value of an attribute</li>
 * <li>{@link #arguments} (optional) - an array of arguments</li>
 * </ul>
 * For more documentation please refer to the JQuery Taconite
 * <a href="http://malsup.com/jquery/taconite/#commands">commands</a> section.
 *
 * <a name="custom-commands"></a>
 * <h3>Creating custom commands</h3>
 *
 * {@link Taconite} defines some predefined commands however you can easily
 * define your own commands. For example if you want to execute the JQuery
 * function <a href="http://docs.jquery.com/Attributes/text#val">text</a>, you can
 * formulate the following Command:
 *
 * <pre class="prettyprint">
 * public void onInit() {
 *
 *     link.setActionListener(new AjaxAdapter()) {
 *
 *         public Partial onAjaxAction(Control source) {
 *             Taconite taconite = new Taconite();
 *
 *             String commandName = "text";
 *             String selector = "#mySpan";
 *             String text = "Hello <b>World</b>";
 *
 *             // Create the new command
 *             Command command = new Command(commandName, selector, text);
 *
 *             // Add the command to Taconite
 *             taconite.add(command);
 *         }
 *     }
 * } </pre>
 */
public class Command {

    // -------------------------------------------------------------- Variables

    /** The command to execute, normally a JQuery function. */
    private String command;

    /** The CSS selector to identify the target of the operation. */
    private String selector;

    /** The name of an attribute. */
    private String name;

    /** The value of an attribute. */
    private String value;

    /** An array of arguments to pass to the command. */
    private List arguments;

    /** The content (payload) of the command. */
    private List content = new ArrayList();

    /** Indicates if the content should be wrapped in a CDATA tag. */
    private boolean characterData = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a Command for the given command value.
     *
     * @param command the command to execute
     */
    public Command(String command) {
        setCommand(command);
    }

    /**
     * Create a Command for the given command and CSS selector.
     *
     * @param command the command to execute
     * @param selector the CSS to identify the target of the operation
     */
    public Command(String command, String selector) {
        setCommand(command);
        setSelector(selector);
    }

    /**
     * Create a Command for the given command, CSS selector and content.
     *
     * @param command the command to execute
     * @param selector the CSS to identify the target of the operation
     * @param content the content of the command
     */
    public Command(String command, String selector, String content) {
        setCommand(command);
        setSelector(selector);
        if (content != null) {
            getContent().add(content);
        }
    }

    /**
     * Create a Command for the given command and Control.
     * <p/>
     * The Control will be set as the target of the operation.
     *
     * @param command the command to execute
     * @param content the content of the command
     */
    public Command(String command, Control content) {
        setCommand(command);
        if (content != null) {
            add(content);
        }
    }

    /**
     * Create a Command for the given command, CSS selector and Control.
     *
     * @param command the command to execute
     * @param selector the CSS to identify the target of the operation
     * @param content the content of the command
     */
    public Command (String command, String selector, Control content) {
        setCommand(command);
        setSelector(selector);
        if (content != null) {
            add(content);
        }
    }

    /**
     * Create a Command for the given command, CSS selector and Element resource.
     *
     * @param command the command to execute
     * @param selector the CSS to identify the target of the operation
     * @param content the content of the command
     */
    public Command(String command, String selector, Element content) {
        setCommand(command);
        setSelector(selector);
        if (content != null) {
            getContent().add(content);
        }
    }

    /**
     * Create a Command for the given command and flag indicating whether
     * the content contains character data that should be wrapped in CDATA tags.
     *
     * @param command the command to execute
     * @param characterData flag indicating if the content contains character
     * data and should be wrapped in the CDATA tag
     */
    public Command(String command, boolean characterData) {
        setCommand(command);
        setCharacterData(characterData);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the tag of the command. This method returns the result of the
     * method {@link #getCommand()}.
     *
     * @return the tag of the command
     */
    public String getTag() {
        return getCommand();
    }

    /**
     * Return true if the Command content should be wrapped in CDATA tags,
     * false otherwise.
     *
     * @return true if the Command content should be wrapped in CDATA tags,
     * false otherwise
     */
    public boolean isCharacterData() {
        return characterData;
    }

    /**
     * Sets whether the Command content should be wrapped in CDATA tags or not.
     *
     * @param characterData true indicates that the Command content should be
     * wrapped in CDATA tags, false otherwise
     */
    public void setCharacterData(boolean characterData) {
        this.characterData = characterData;
    }

    /**
     * Sets whether the Command content should be wrapped in CDATA tags or not.
     *
     * @param characterData true indicates that the Command content should be
     * wrapped in CDATA tags, false otherwise
     * @return this command instance
     */
    public Command characterData(boolean characterData) {
        setCharacterData(characterData);
        return this;
    }

    /**
     * Add the array of arguments to the Command list of {@link #arguments}.
     *
     * @param args an array of arguments to add
     * @return this command instance
     */
    public Command arguments(String... args) {
        getArguments().addAll(Arrays.asList(args));
        return this;
    }

    /**
     * Add the argument to the Command list of {@link #arguments}.
     *
     * @param arg the argument to add
     * @return this command instance
     */
    public Command argument(String arg) {
        return addArgument(arg);
    }

    /**
     * Add the name/value pair arguments to the Command list of {@link #arguments}.
     *
     * @param name the "name" argument to add
     * @param value the "value" argument to add
     * @return this command instance
     */
    public Command argument(String name, String value) {
        addArgument(name);
        return addArgument(value);
    }

    /**
     * Add the argument to the Command list of {@link #arguments}.
     *
     * @param arg the argument to add
     * @return this command instance
     */
    public Command addArgument(String arg) {
        getArguments().add(arg);
        return this;
    }

    /**
     * Add the name/value pair arguments to the Command list of {@link #arguments}.
     *
     * @param name the "name" argument to add
     * @param value the "value" argument to add
     * @return this command instance
     */
    public Command addArgument(String name, String value) {
        getArguments().add(name);
        getArguments().add(value);
        return this;
    }

    /**
     * Remove the argument from the Command list of {@link #arguments}.
     *
     * @param arg the argument to remove
     * @return this command instance
     */
    public Command removeArgument(String arg) {
        getArguments().remove(arg);
        return this;
    }

    /**
     * Return the command's arguments.
     *
     * @return the command's arguments.
     */
    public List getArguments() {
        if (arguments == null) {
            arguments = new ArrayList();
        }
        return arguments;
    }

    /**
     * Return true if the command contains any arguments, false otherwise.
     *
     * @return true if the command contains any arguments, false otherwise
     */
    public boolean hasArguments() {
        if (arguments != null) {
            return !getArguments().isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Return the {@link #command} to execute.
     *
     * @return the command to execute
     */
    public String getCommand() {
        return command;
    }

    /**
     * Set the {@link #command} to execute.
     *
     * @param command the command to execute
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Set the {@link #command} to execute.
     *
     * @param command the command to execute
     * @return this command instance
     */
    public Command command(String command) {
        setCommand(command);
        return this;
    }

    /**
     * Return the name attribute of the command.
     *
     * @return the name attribute of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name attribute of the command.
     *
     * @param name the name attribute of the command
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the name attribute of the command.
     *
     * @param name the name attribute of the command
     * @return this command instance
     */
    public Command name(String name) {
        setName(name);
        return this;
    }

    /**
     * Return an attribute value of the command.
     *
     * @return an attribute value of the command
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the attribute value of the command.
     *
     * @param value the attribute value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Set the value attribute of the command.
     *
     * @param value the attribute's value for the command
     * @return this command instance
     */
    public Command value(String value) {
        setValue(value);
        return this;
    }

    /**
     * Return the CSS selector.
     *
     * @return the CSS selector
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Set the CSS selector.
     *
     * @param selector the CSS selector
     */
    public void setSelector(String selector) {
        this.selector = selector;
    }

    /**
     * Set the CSS selector.
     *
     * @param selector the CSS selector
     * @return this command instance
     */
    public Command selector(String selector) {
        setSelector(selector);
        return this;
    }

    /**
     * Return the Command list of content.
     *
     * @return the command list of content
     */
    public List getContent() {
        return content;
    }

    /**
     * Set the command list of content.
     *
     * @param content the command list of content
     */
    public void setContent(List content) {
        this.content = content;
    }

    /**
     * Set the command list of content.
     *
     * @param content the command list of content
     * @return this command instance
     */
    public Command content(List content) {
        this.content = content;
        return this;
    }

    /**
     * Add the String content to the command content list.
     *
     * @param content the content to add
     * @return this command instance
     */
    public Command add(String content) {
        return insert(content, getContent().size());
    }

    /**
     * Add the Control to the command content list.
     *
     * @param control the Control to add
     * @return this command instance
     */
    public Command add(Control control) {
        return insert(control, getContent().size());
    }

    /**
     * Add the Element resource to the command content list.
     *
     * @param resource the Element to add
     * @return this command instance
     */
    public Command add(Element resource) {
        return insert(resource, getContent().size());
    }

    /**
     * Add the String content to the command content list at the given index.
     *
     * @param content the String content to add
     * @param index the index position where to add the content to
     * @return this command instance
     */
    public Command insert(String content, int index) {
        getContent().add(index, content);
        return this;
    }

    /**
     * Add the Control to the command content list at the given index.
     *
     * @param control the Control to add
     * @param index the index position where to add the Control to
     * @return this command instance
     */
    public Command insert(Control control, int index) {
        if (getSelector() == null) {
            setSelector(getSelector(control));
        }
        getContent().add(index, control);
        return this;
    }

    /**
     * Add the Element resource to the command content list at the given index.
     *
     * @param resource the Element to add
     * @param index the index position where to add the Element to
     * @return this command instance
     */
    public Command insert(Element resource, int index) {
        getContent().add(index, resource);
        return this;
    }

    /**
     * Remove the content from the command content list.
     *
     * @param content the content to remove
     * @return this command instance
     */
    public Command remove(String content) {
        getContent().remove(content);
        return this;
    }

    /**
     * Remove the control from the command content list.
     *
     * @param control the Control to remove
     * @return this command instance
     */
    public Command remove(Control control) {
        getContent().remove(control);
        return this;
    }

    /**
     * Remove the Element from the command content list.
     *
     * @param resource the Element to remove
     * @return this command instance
     */
    public Command remove(Element resource) {
        getContent().remove(resource);
        return this;
    }

    /**
     * Render the XML representation of the command.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {
        String tag = getTag();
        renderTagBegin(tag, buffer);

        if (getContent().isEmpty()) {
            buffer.elementEnd();
        } else {

            buffer.closeTag();

            // NOTE: renderContent must start rendering on same line as opening tag
            // incase an EVAL command is specified and content is wrapped in CDATA.
            renderContent(buffer);

            renderTagEnd(tag, buffer);
        }
    }

    /**
     * Render the XML representation of the command.
     *
     * @return the String representation of the command
     */
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(getContent().size() + 50);
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Get the CSS selector for the given control.
     *
     * @param control the control to retrieve the CSS selector from
     * @return the control CSS selector
     */
    protected String getSelector(Control control) {
        String selector = AjaxUtils.getSelector(control);
        if (selector == null) {
            throw new IllegalArgumentException("No selector could be found for"
                + " the control: " + control.getClass().getName() + "#"
                + control.getName());
        }
        return selector;
    }

    /**
     * Render the tag and the attributes {@link #selector}, {@link #name},
     * {@link #value} and {@link #arguments}.
     * <p/>
     * <b>Please note:</b> the tag will not be closed by this method. This
     * enables callers of this method to append extra attributes.
     *
     * @param tagName the name of the tag to render
     * @param buffer the buffer to render the output to
     */
    protected void renderTagBegin(String tagName, HtmlStringBuffer buffer) {
        buffer.elementStart(tagName);
        buffer.appendAttribute("select", getSelector());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("value", getValue());
        if (hasArguments()) {
            List args = getArguments();
            for(int i = 0; i < args.size(); i++) {
                String arg = (String) args.get(i);
                buffer.append(" arg").append(i + 1);
                buffer.append("=\"");
                buffer.append(arg);
                buffer.append("\"");
            }
        }
    }

    /**
     * Render this command content to the specified buffer.
     *
     * @param buffer the buffer to append the output to
     */
    protected void renderContent(HtmlStringBuffer buffer) {
        boolean wrapInCDATA = false;
        if ((Taconite.EVAL.equals(getCommand()) && getContent().size() > 0)
            || isCharacterData()) {
            wrapInCDATA = true;
        }

        if (wrapInCDATA) {
            // NB: For Firefox <![CDATA MUST immediately proceed the <eval> tag,
            // there should be no newlines \n or blanks " ".
            buffer.append("<![CDATA[ ");
        }

        Iterator it = getContent().iterator();
        while(it.hasNext()) {
            Object content = it.next();
            if (content instanceof Control) {
                ((Control) content).render(buffer);
            } else if (content instanceof Element) {
                ((Element) content).render(buffer);
            } else {
                buffer.append(content.toString());
            }
            buffer.append("\n");
        }

        if (wrapInCDATA) {
            buffer.append(" ]]>");
        }
    }

    /**
     * Close the specifies tag.
     *
     * @param tagName the name of the tag to close
     * @param buffer the buffer to append the output to
     */
    protected void renderTagEnd(String tagName, HtmlStringBuffer buffer) {
        buffer.elementEnd(tagName);
    }
}
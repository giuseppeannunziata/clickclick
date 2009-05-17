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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.Control;
import org.apache.click.MockContext;
import org.apache.click.element.Element;
import org.apache.click.extras.control.DateField;
import org.apache.click.util.HtmlStringBuffer;

public class Command {

    private String command;
    
    private String name;
    
    private String value;
    
    private String select;
    
    private List arguments;

    private List content = new ArrayList();

    private boolean characterData = false;

    public Command(String command) {
        setCommand(command);
    }
    
    public Command(String command, String select) {
        setCommand(command);
        setSelect(select);
    }
    
    public Command(String command, String select, String content) {
        setCommand(command);
        setSelect(select);
        if (content != null) {
            getContent().add(content);
        }
    }
    
    public Command(String command, Control control) {
        setCommand(command);
        if (control != null) {
            add(control);
        }
    }

    public Command (String command, String select, Control control) {
        setCommand(command);
        setSelect(select);
        if (control != null) {
            add(control);
        }
    }

    public Command(String command, String select, Element resource) {
        setCommand(command);
        setSelect(select);
        if (resource != null) {
            getContent().add(resource);
        }
    }

    public Command(String command, boolean containingSpecialChars) {
        setCommand(command);
        setCharacterData(containingSpecialChars);
    }

    // --------------------------------------------------------- Public Methods

    public String getTag() {
        return getCommand();
    }
    
    public Command arg(String arg) {
        return addArgument(arg);
    }
    
    public Command addArgument(String arg) {
        getArguments().add(arg);
        return this;
    }

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

    public boolean hasArguments() {
        if (arguments != null) {
            return !getArguments().isEmpty();
        } else {
            return false;
        }
    }

    public String getCommand() {
        return command;
    }

    public Command setCommand(String command) {
        this.command = command;
        return this;
    }

    public Command name(String name) {
        return setName(name);
    }

    public String getName() {
        return name;
    }

    public Command setName(String name) {
        this.name = name;
        return this;
    }

    public Command value(String value) {
        return setValue(value);
    }

    public String getValue() {
        return value;
    }

    public Command setValue(String value) {
        this.value = value;
        return this;
    }

    public Command select(String select) {
        return setSelect(select);
    }

    public String getSelect() {
        return select;
    }

    public Command setSelect(String select) {
        this.select = select;
        return this;
    }

    public List getContent() {
        return content;
    }

    public Command add(String content) {
        return insert(content, getContent().size());
    }

    public Command add(Control control) {
        return insert(control, getContent().size());
    }

    public Command add(Element resource) {
        return insert(resource, getContent().size());
    }

    public Command insert(String content, int index) {
        getContent().add(index, content);
        return this;
    }

    public Command insert(Control control, int index) {
        if (getSelect() == null) {
            setSelect(getSelector(control));
        }
        getContent().add(index, control);
        return this;
    }

    public Command insert(Element resource, int index) {
        getContent().add(index, resource);
        return this;
    }

    public Command add(Element resource, int index) {
        getContent().add(index, resource);
        return this;
    }

    public Command remove(String content) {
        getContent().remove(content);
        return this;
    }

    public Command remove(Control control) {
        getContent().remove(control);
        return this;
    }

    public Command remove(Element resource) {
        getContent().remove(resource);
        return this;
    }

    public Command setContent(List content) {
        this.content = content;
        return this;
    }

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

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(getContent().size() + 50);
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
        buffer.appendAttribute("select", getSelect());
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

    protected void renderTagEnd(String tagName, HtmlStringBuffer buffer) {
        buffer.elementEnd(tagName);
    }

    public static void main(String[] args) {
        MockContext.initContext();
        Taconite taconite = new Taconite();
        Command command = new Command(Taconite.APPEND);
        taconite.add(command);
        command.add(new DateField("date"));
        System.out.println(taconite);
    }

    public boolean isCharacterData() {
        return characterData;
    }

    public Command setCharacterData(boolean characterData) {
        this.characterData = characterData;
        return this;
    }
}

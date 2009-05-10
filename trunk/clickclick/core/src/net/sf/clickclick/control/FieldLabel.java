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
package net.sf.clickclick.control;

import org.apache.click.control.AbstractControl;
import org.apache.click.control.Field;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * This control renders a <em>label</em> for a target field. If no label is
 * specified the label will be rendered as an empty String.
 * <p/>
 * The following example shows how to use a FieldLabel control to render a
 * <tt>label</tt> for a given Field.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         Field field = new TextField("my-field");
 *
 *         // Create a new FieldLabel for the field
 *         FieldLabel fieldLabel = new FieldLabel("my-field-label", field, "My Field");
 *
 *         // Add the FieldLabel to the Page control list
 *         addControl(fieldLabel);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $my-field-label $field</pre>
 *
 * will render as:
 *
 * <div class="border">
 * <label for="my-field-label">My Field: </label><input type="text" name="my-field" id="my-field"/>
 * </div>
 *
 * @author Bob Schellink
 */
public class FieldLabel extends AbstractControl {

    /** The target Field object. */
    private Field target;

    /** The label to render for the Field. */
    private String label;

    /**
     * Create a default FieldLabel instance.
     */
    public FieldLabel() {
    }

    /**
     * Create a FieldLabel with the given name.
     *
     * @param name the name of the FieldLabel
     */
    public FieldLabel(String name) {
        super(name);
    }

    /**
     * Create a FieldLabel for the given field.
     *
     * @param target the target field
     */
    public FieldLabel(Field target) {
        this(target, ClickUtils.toLabel(target.getName()), null);
    }

    /**
     * Create a FieldLabel for the given name and field.
     *
     * @param name the name of the FieldLabel
     * @param target the target field
     */
    public FieldLabel(String name, Field target) {
        this(name, target, ClickUtils.toLabel(target.getName()), null);
    }

    /**
     * Create a FieldLabel for the given field and label.
     *
     * @param target the target field
     * @param label the label to render for the field
     */
    public FieldLabel(Field target, String label) {
        this(target, label, null);
    }

    /**
     * Create a FieldLabel for the given field and label.
     *
     * @param name the name of the FieldLabel
     * @param target the target field
     * @param label the label to render for the field
     */
    public FieldLabel(String name, Field target, String label) {
        this(target, label, null);
    }

    /**
     * Create a FieldLabel for the given field, label and HTML
     * <tt>accesskey</tt> attribute.
     *
     * @param target the target field
     * @param label the label to render for the field
     * @param accesskey the HTML accesskey attribute
     */
    public FieldLabel(Field target, String label, String accesskey) {
        this(null, target, label, accesskey);
    }

    /**
     * Create a FieldLabel for the given name, field, label and HTML
     * <tt>accesskey</tt> attribute.
     *
     * @param name the name of the FieldLabel
     * @param target the target field
     * @param label the label to render for the field
     * @param accesskey the HTML accesskey attribute
     */
    public FieldLabel(String name, Field target, String label, String accesskey) {
        super(name);
        this.target = target;
        this.label = label + ":";
        if (accesskey != null) {
            setAttribute("accesskey", accesskey);
        }
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the FieldLabel html tag: <tt>label</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "label";
    }

    /**
     * Return the target field.
     *
     * @return the target field
     */
    public Field getTarget() {
        return target;
    }

    /**
     * Set the target field.
     *
     * @param target the target field
     */
    public void setTarget(Field target) {
        this.target = target;
    }

    /**
     * Return the label of the target field.
     *
     * @return the label of the target field
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label of the target field.
     *
     * @param label the label of the target field
     */
    public void setLabel(String label) {
        this.label = label;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the HTML representation of the FieldLabel. The FieldLabel is
     * rendered as an HTML "&ltlabel&gt; element e.g:
     * "&lt;label for="firstnameId">Firstname:&lt;/label&gt;.
     *
     * @param buffer the specified buffer to render the control's output to
     */
    public void render(HtmlStringBuffer buffer) {
        Field target = getTarget();
        if (target == null) {
            throw new IllegalStateException("Target cannot be null.");
        }

        String label = getLabel();
        if (label == null) {
            label = "";
        }

        // Open tag: <label
        buffer.elementStart(getTag());

        // Set attribute to target field's id
        setAttribute("for", target.getId());

        // Render all the labels attributes
        appendAttributes(buffer);

        // Close tag: <label for="firstname">
        buffer.closeTag();

        // Add label text: <label for="firstname">Firstname:
        buffer.append(label);

        // Close tag: <label for="firstname">Firstname:</label>
        buffer.elementEnd(getTag());
    }
}

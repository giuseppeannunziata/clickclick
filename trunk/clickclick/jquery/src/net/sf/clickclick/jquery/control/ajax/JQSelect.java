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
package net.sf.clickclick.jquery.control.ajax;

import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.control.Select;

/**
 * Provide an Ajax enabled Select control.
 * <p/>
 * <b>Please note:</b> JQSelect uses {@link net.sf.clickclick.jquery.helper.JQHelper}
 * to provide the Ajax functionality.
 * <p/>
 * Below is an example showing how to use a JQSelect:
 *
 * <pre class="prettyprint">
 * private Form form = new Form("form");
 *
 * public MyPage() {
 *     addControl(form);
 *
 *     JQSelect select = new JQSelect("select");
 *
 *     // Set an Ajax listener on the select that return a Taconite (Partial)
 *     // instance
 *     select.setActionListener(new AjaxAdapter() {
 *         public Partial onAjaxAction(Control source) {
 *             Taconite partial = new Taconite();
 *
 *             // When a select option is changed, we simply print the value.
 *             // Taconite allows one to execute any JavaScript code using the
 *             // method "eval".
 *             partial.eval("$('#myTarget').html('You have picked " + select.getValue() + "');");
 *
 *             return partial;
 *         }
 *     });
 *
 *     form.add(select);
 * } </pre>
 */
public class JQSelect extends Select {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    private JQHelper jqHelper = new JQHelper(this);

    // ------------------------------------------------------------ Constructor

    /**
     * Create a default JQSelect field.
     */
    public JQSelect() {
    }

    /**
     * Create a JQSelect field with the given name.
     *
     * @param name the name of the control
     */
    public JQSelect(String name) {
        super(name);
    }

    /**
     * Create a JQSelect field with the given name and label.
     *
     * @param name the name of the control
     * @param label the label of the control
     */
    public JQSelect(String name, String label) {
        super(name, label);
    }

    /**
     * Create a JQSelect field with the given name and required status.
     *
     * @param name the name of the field
     * @param required the field required status
     */
    public JQSelect(String name, boolean required) {
        super(name, required);
    }

    /**
     * Create a JQSelect field with the given name, label and required status.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param required the field required status
     */
    public JQSelect(String name, String label, boolean required) {
        super(name, label, required);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the JQuery Helper instance.
     *
     * @return the jqHelper instance
     */
    public JQHelper getJQueryHelper() {
        return jqHelper;
    }

    /**
     * Set the JQuery Helper instance.
     *
     * @param jqHelper the JQuery Helper instance
     */
    public void setJQueryHelper(JQHelper jqHelper) {
        this.jqHelper = jqHelper;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize the JQSelect Ajax functionality.
     */
    public void onInit() {
        super.onInit();
        jqHelper.setEvent("change");
        jqHelper.ajaxify();
    }
}

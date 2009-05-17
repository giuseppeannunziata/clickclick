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
package net.sf.clickclick.jquery.helper;

import java.util.List;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.element.JsImport;

/**
 * Provide a specialized JQuery helper that Ajax enables a target Form.
 *
 * @author Bob Schellink
 */
public class JQFormHelper extends JQHelper {

    // -------------------------------------------------------------- Constants

    /**
     * The JQuery Form plugin (http://www.malsup.com/jquery/form/):
     * "<tt>/clickclick/jquery/form/jquery-form.js</tt>".
     */
    public static String jqueryFormImport = "/clickclick/jquery/form/jquery.form.js";

    // -------------------------------------------------------------- Variables

    /**
     * The path of the template to render:
     * "<tt>/clickclick/jquery/template/jquery.form.js</tt>".
     */
    private String template = "/clickclick/jquery/template/jquery.form.template.js";

    // ----------------------------------------------------------- Cosntructors

    /**
     * Create a JQFormHelper for the given target Form.
     *
     * @param form the helper target Form
     */
    public JQFormHelper(Form form) {
        super(form);
        setTemplate(template);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable the target Form to submit Ajax requests.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable the target Form to submit Ajax requests
     */
    protected void addHeadElements(List headElements) {
        super.addHeadElements(headElements);
        JsImport jsImport = new JsImport(jqueryFormImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
        }

        // Add the Form ID as a HiddeField to trigger Ajax callback
        Form form = (Form) getControl();
        if (form.getField(form.getId()) == null) {
            form.add(new HiddenField(form.getId(), "1"));
        }
    }
}

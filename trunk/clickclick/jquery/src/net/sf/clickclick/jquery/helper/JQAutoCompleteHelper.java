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
import org.apache.click.Control;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

/**
 * Provide a specialized JQuery helper that adds auto complete functionality
 * to a target Field control.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.autocomplete.template.js.txt">here</a>
 * to view the template.
 * <p/>
 * When text is entered in a field an Ajax request is made back to the server
 * with the entered text as a request parameter. On the server a list of
 * suggested completions is compiled, based on the entered text, and send back
 * to the browser.
 * <p/>
 * <b>Please note: </b> suggestions must be separated by newline ('\n')
 * character.
 * <p/>
 * JQAutoCompleteHelper can either be embedded inside a custom Field, or used
 * to decorate the Field.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom Field with an embedded JQAutoCompleteHelper
 * that enables Ajax behavior:
 *
 * <pre class="prettyprint">
 * public class JQAutoCompleteTextField extends TextField {
 *
 *     // The embedded JQuery AutoComplete helper object.
 *     private JQHelper jqHelper = new JQAutoCompleteHelper(this);
 *
 *     // Constructor
 *     public JQAutoCompleteTextField(String name) {
 *         super(name);
 *     }
 *
 *     // Initialize the Ajax functionality
 *     public void onInit() {
 *         super.onInit();
           jqHelper.ajaxify();
 *     }
 * } </pre>
 *
 * Below is an example how to decorate a TextField to retrieve suggestions every
 * time the user enters text:
 *
 * <pre class="prettyprint">
 * public class AutoCompleteDemo extends BorderPage {
 *
 *     private Form form = new Form("form");
 *
 *     public AutoCompleteDemo() {
 *         addControl(form);
 *
 *         final TextField countryField = new TextField("country");
 *         form.add(countryField);
 *
 *         // Decorate the countryField with Ajax functionality
 *         JQAutoCompleteHelper jquery = new JQAutoCompleteHelper(countryField);
 *         jquery.ajaxify();
 *
 *         // Register an Ajax listener on the form which is invoked when the
 *         // form is submitted.
 *         countryField.setActionListener(new AjaxAdapter() {
 *             public Partial onAjaxAction(Control source) {
 *
 *                 // Create a Partial to contains the auto complete suggestions
 *                 Partial partial = new Partial();
 *
 *                 String criteria = countryField.getValue();
 *
 *                 // Retrieve suggestions for the given criteria. Each suggestion
 *                 // should be separated by a newline char: '\n'.
 *                 String suggestions = getSuggestions(criteria);
 *
 *                 partial.setContent(suggestions);
 *
 *                 return partial;
 *             }
 *         });
 *     }
 * } </pre>
 *
 * @author Bob Schellink
 */
public class JQAutoCompleteHelper extends JQHelper {

    // -------------------------------------------------------------- Constants

    /**
     * The JQuery Autocomplete plugin JavaScript import (http://bassistance.de/jquery-plugins/jquery-plugin-autocomplete/):
     * "<tt>/clickclick/jquery/autocomplete/jquery.autocomplete.js</tt>".
     */
    public static String jqueryAutoCompleteJsImport
        = "/clickclick/jquery/autocomplete/jquery.autocomplete.js";

    /**
     * The JQuery Autocomplete plugin CSS import (http://bassistance.de/jquery-plugins/jquery-plugin-autocomplete/):
     * "<tt>/clickclick/jquery/autocomplete/jquery.autocomplete.css</tt>".
     */
    public static String jqueryAutocompleteCssImport
        = "/clickclick/jquery/autocomplete/jquery.autocomplete.css";

    // -------------------------------------------------------------- Variables

    /**
     * The path of the AutoComplete template to render:
     * "<tt>/clickclick/jquery/template/jquery.autocomplete.template.js</tt>".
     */
    private String template = "/clickclick/jquery/template/jquery.autocomplete.template.js";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new JQAutoCompleteHelper for the given target control.
     *
     * @param control the helper target control
     */
    public JQAutoCompleteHelper(Control control) {
        this(control, null);
    }

    /**
     * Create a new JQAutoCompleteHelper for the given target control and CSS
     * selector.
     * <p/>
     * Although any valid CSS selector can be specified, the CSS selector
     * usually specifies the HTML ID attribute of a target element on the page
     * eg: "<tt>#form-id</tt>".
     *
     * @param control the helper target control
     * @param selector the CSS selector
     */
    public JQAutoCompleteHelper(Control control, String select) {
        super(control, select);
        setTemplate(template);
        setShowIndicator(false);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable AutoComplete functionality.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable AutoComplete functionality
     */
    protected void addHeadElements(List headElements) {
        JsImport jsImport = new JsImport(jqueryImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(0, jsImport);
        }

        jsImport = new JsImport(jqueryAutoCompleteJsImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(jsImport);
        }

        CssImport cssImport = new CssImport(jqueryAutocompleteCssImport);
        if (!headElements.contains(cssImport)) {
            headElements.add(cssImport);
        }

        addTemplate(headElements);
    }
}

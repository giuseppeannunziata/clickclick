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
import net.sf.clickclick.AjaxControlRegistry;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

/**
 * Provide a specialized JQuery helper that adds auto complete functionality
 * to a target Field control.
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

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
package net.sf.clickclick.examples.jquery.page.ajax;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQAutoCompleteTextField;
import net.sf.clickclick.jquery.helper.JQAutoCompleteHelper;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 */
public class AdvancedAutoComplete extends BorderPage {

    private Form form = new Form("form");

    public AdvancedAutoComplete() {
        addControl(form);

        final JQAutoCompleteTextField suburbField = new JQAutoCompleteTextField("suburb", "Select your suburb") {

            public List getAutoCompleteList(String criteria) {
                List suggestions = getPostCodeService().getPostCodeLocations(criteria);
                return suggestions;
            }
        };
        suburbField.setWidth("200px");

        // Set formatResult option to set the field value to the suburb
        suburbField.getJQueryHelper().setOptions("formatResult: function(data) {"
            + " var values = data[0].split(' ');"
            + " var result = '';"
            + " for(var i = 0; i < values.length - 2; i++){"
            + "   result+=values[i]+' '"

            // Trim whitespaces and left over ','
            + " } return result.replace(/^\\s*/, '').replace(/,\\s*$/, '');"
            + "}");

        final TextField postCodeField = new TextField("postalCode", "Select your postal code");
        JQAutoCompleteHelper jquery = new JQAutoCompleteHelper(postCodeField);

        // Decorate the textField with Autocomplete functionality
        jquery.ajaxify();

        postCodeField.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                String criteria = postCodeField.getValue();

                // Create a Partial to contain the auto complete suggestions
                Partial partial = new Partial();

                HtmlStringBuffer buffer = new HtmlStringBuffer();

                // Retrieve suggestions
                List<String> suggestions = getPostCodeService().getPostCodeLocations(criteria);

                // Remove duplicates
                Set<String> unique = new LinkedHashSet(suggestions);
                for (String code : unique) {
                    buffer.append(code).append("\n");
                }
                partial.setContent(buffer.toString());
                return partial;
            }
        });
        postCodeField.setWidth("200px");

        // Set formatResult option to set the field value to the post code
        jquery.setOptions("formatResult: function(data) {"
            + " var values = data[0].split(' ');"
            + " return values[values.length - 1];}");

        Submit submit = new Submit("submit");
        submit.setActionListener(new ActionListener() {

            public boolean onAction(Control source) {
                if (form.isValid()) {
                    // save form data
                }
                return true;
            }
        });

        form.add(submit);
        form.add(suburbField);
        form.add(postCodeField);
    }
}

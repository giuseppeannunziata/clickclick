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

import java.util.ArrayList;
import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQAutoCompleteTextField;
import net.sf.clickclick.jquery.helper.JQAutoCompleteHelper;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.extras.control.IntegerField;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 * @author Bob Schellink
 */
public class AdvancedAutoComplete extends BorderPage {

    private Form form = new Form("form");

    public AdvancedAutoComplete() {
        addControl(form);

        final JQAutoCompleteTextField autoField = new JQAutoCompleteTextField("autoField") {

            public List getAutoCompleteList(String criteria) {
                List suggestions = new ArrayList();
                suggestions.add("one");
                suggestions.add("two");
                suggestions.add("three");
                return suggestions;
            }
        };

        final IntegerField integerField = new IntegerField("integerField");
        JQAutoCompleteHelper jquery = new JQAutoCompleteHelper(integerField);
        jquery.ajaxify();
        integerField.setActionListener(new AjaxAdapter(){
            public Partial onAjaxAction(Control source) {
                System.out.println("Ajax 2");
                Partial partial = new Partial();
                HtmlStringBuffer buffer = new HtmlStringBuffer();
                buffer.append("601020\n");
                buffer.append("101020\n");
                buffer.append("100\n");
                partial.setContent(buffer.toString());
                return partial;
            }
        });

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
        form.add(autoField);
        form.add(integerField);
    }
}

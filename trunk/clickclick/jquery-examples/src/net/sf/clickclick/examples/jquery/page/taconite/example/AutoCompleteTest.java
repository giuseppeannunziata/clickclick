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
package net.sf.clickclick.examples.jquery.page.taconite.example;

import java.util.ArrayList;
import java.util.List;
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
public class AutoCompleteTest extends BorderPage {

    public AutoCompleteTest() {
        Form form = new Form("form");
        addControl(form);

        final JQAutoCompleteTextField field1 = new JQAutoCompleteTextField("field1") {

            public List getAutoCompleteList(String criteria) {
                List suggestions = new ArrayList();
                suggestions.add("one");
                suggestions.add("two");
                suggestions.add("three");
                return suggestions;
            }
        };

        final TextField field2 = new TextField("field2");
        JQAutoCompleteHelper jquery = new JQAutoCompleteHelper(field2);
        jquery.ajaxify();
        field2.setActionListener(new AjaxAdapter(){
            public Partial onAjaxAction(Control source) {
                System.out.println("Ajax 2");
                Partial partial = new Partial();
                HtmlStringBuffer buffer = new HtmlStringBuffer();
                buffer.append("blah\n");
                buffer.append("pok\n");
                partial.setContent(buffer.toString());
                return partial;
            }
        });

        Submit submit = new Submit("submit");
        submit.setActionListener(new ActionListener() {
            public boolean onAction(Control source) {
                System.out.println("On Action Clicked!");
                return true;
            }
        });

        form.add(submit);
        form.add(field1);
        form.add(field2);
    }
}

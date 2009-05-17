package net.sf.clickclick.examples.jquery.page.ajax;

import java.util.ArrayList;
import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQAutoCompleteTextField;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;

/**
 *
 * @author bob
 */
public class AutoComplete extends BorderPage {

    private Form form = new Form("form");

    public AutoComplete() {
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
    }
}

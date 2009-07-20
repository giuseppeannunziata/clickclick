package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionButton;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.commons.lang.math.NumberUtils;

public class ActionButtonDemo extends BorderPage {

    private JQActionButton button = new JQActionButton("button", "Counter");

    public ActionButtonDemo() {
        addControl(button);
        button.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                incrementCounter();

                // Replace the button with the updated button
                partial.replace(button);

                // Using a CSS selector, replace the target counter with the
                // updated button value
                partial.replaceContent("#target", button.getValue());
                return partial;
            }
        });
    }

    private void incrementCounter() {
        int value = NumberUtils.toInt(button.getValue());
        value++;
        button.setValue(Integer.toString(value));
    }
}

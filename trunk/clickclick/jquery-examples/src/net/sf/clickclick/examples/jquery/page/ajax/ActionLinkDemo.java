package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQActionLink;
import net.sf.clickclick.jquery.util.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.commons.lang.math.NumberUtils;

public class ActionLinkDemo extends BorderPage {

    private JQActionLink link = new JQActionLink("link", "Counter", "linkId");

    public ActionLinkDemo() {
        addControl(link);
        link.getJQueryHelper().setIndicatorTarget("#div");
        link.getJQueryHelper().setIndicatorMessage("<h1><img src=\"/jquery-examples/assets/images/indicator.gif\"/></h1>");
        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                incrementCounter();

                // Replace the link with the updated link
                partial.replace(link);

                // Using a CSS selector, replace the target counter with the
                // updated link value
                partial.replaceContent("#target", link.getValue());
                return partial;
            }
        });
    }

    private void incrementCounter() {
        int value = NumberUtils.toInt(link.getValue());
        value++;
        link.setValue(Integer.toString(value));
    }
}

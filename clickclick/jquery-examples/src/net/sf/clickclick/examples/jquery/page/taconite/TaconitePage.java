package net.sf.clickclick.examples.jquery.page.taconite;

import org.apache.click.Control;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Taconite;
import org.apache.click.Page;
import org.apache.click.extras.control.DateField;

/**
 *
 * @author Bob Schellink
 */
public class TaconitePage extends Page {

    private JQActionLink link = new JQActionLink("link");

    public void onInit() {
        super.onInit();

        Div wrapper = new Div("dateWrapper");
        final String wrapperId = "wrapper_id";
        wrapper.setId(wrapperId);
        wrapper.add(new DateField("date"));

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                partial.replaceContent('#' + wrapperId, new DateField("date"));
                return partial;
            }
        });

        addControl(wrapper);
        addControl(link);
    }
}

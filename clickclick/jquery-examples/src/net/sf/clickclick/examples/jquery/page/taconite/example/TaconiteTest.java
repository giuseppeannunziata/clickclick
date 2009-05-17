package net.sf.clickclick.examples.jquery.page.taconite.example;

import java.util.ArrayList;
import java.util.List;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.extras.control.DateField;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.jquery.controls.ajax.JQActionLink;
import net.sf.clickclick.jquery.util.Taconite;

/**
 *
 * @author Bob Schellink
 */
public class TaconiteTest extends Page {

    private JQActionLink link = new JQActionLink("link");

    private static final List SORTABLE_OPTIONS = new ArrayList();

    static {
        for (int i = 1; i <= 6; i++) {
            SORTABLE_OPTIONS.add(new Option(Integer.toString(i),
                "Drag to sort me " + i));
        }
    }

    public void onInit() {
        Form form = new Form("form");
        Div wrapper = new Div();
        final String wrapperId = "date_wrapper";
        wrapper.setId(wrapperId);
        wrapper.add(new DateField("date"));
        form.add(wrapper);
        addControl(form);

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                partial.replaceContent('#' + wrapperId, new DateField("date"));
                return partial;
            }
        });

        addControl(link);
    }
}

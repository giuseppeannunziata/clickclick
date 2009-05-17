package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Field;
import org.apache.click.control.TextField;

/**
 * Demonstrates how to update a label while editing a Field using Ajax.
 *
 * In this demo the JQHelper is used to "decorate" a TextField with Ajax
 * functionality.
 *
 * @author Bob Schellink
 */
public class FieldDemo extends BorderPage {

    private Field field = new TextField("field");
    private Span label = new Span("label", "label");

    public FieldDemo() {

        // Register an Ajax listener on the field which is invoked on every
        // "keyup" event.
        field.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                // Set the label content to the latest field value
                label.add(new Text(field.getValue()));
 
                // Replace the label in the browser with the new one
                partial.replace(label);
                return partial;
            }
        });

        JQHelper helper = new JQHelper(field);

        // Switch off the Ajax busy indicator
        helper.setShowIndicator(false);

        // Delay Ajax invoke for 350 millis, otherwise too many calls are made
        // to the server
        helper.setThreshold(350);

        // Set Ajax to fire on keyup events
        helper.setEvent("keyup");

        // Ajaxify the the Field
        helper.ajaxify();

        addControl(field);
        addControl(label);
    }
}

package net.sf.clickclick.examples.jquery.page.basic.tooltip1;

import java.util.HashMap;
import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.control.Form;
import org.apache.click.control.TextField;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;

/**
 *
 */
public class TooltipPage extends BorderPage {

    public String title = "Basic";

    public void onInit() {
        Form form = new Form("form");

        TextField field = new TextField("firstname");

        // Tooltip is generated from the field title. Tooltip heading and body
        // is separated by the pipe '|' character
        field.setTitle("First Name|Enter your firstname");
        form.add(field);

        field = new TextField("lastname");
        field.setTitle("Last Name|Enter your lastname");
        form.add(field);

        field = new TextField("age");
        field.setTitle("Age|Enter your age");
        form.add(field);

        addControl(form);
    }

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import jquery.js, basic.js and basic.css
            headElements.add(new JsImport(JQHelper.jqueryImport));
            headElements.add(new JsImport("/clickclick/example/tooltip1/jquery.cluetip.js"));
            headElements.add(new CssImport("/clickclick/example/tooltip1/jquery.cluetip.css"));
            headElements.add(new JsScript("/basic/tooltip1/tooltip.js", new HashMap()));
        }
        return headElements;
    }
}

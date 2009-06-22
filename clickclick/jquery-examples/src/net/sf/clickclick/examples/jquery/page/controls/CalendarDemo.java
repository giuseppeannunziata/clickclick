package net.sf.clickclick.examples.jquery.page.controls;

import java.util.List;
import java.util.Map;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ui.UICalendarField;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.element.JsScript;
import org.apache.click.util.ClickUtils;

public class CalendarDemo extends BorderPage {

    public CalendarDemo() {
        Form form = new Form("form");
        addControl(form);

        form.add(new TextField("firstname"));
        form.add(new TextField("lastname"));
        form.add(new UICalendarField("calendar"));

        form.add(new Submit("submit"));
    }

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Add calendar-demo.js script
            Map model = ClickUtils.createTemplateModel(this, getContext());
            headElements.add(new JsScript("/controls/calendar-demo.js", model));
        }
        return headElements;
    }
}

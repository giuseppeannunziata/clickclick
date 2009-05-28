package net.sf.clickclick.examples.jquery.page.controls;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ui.UICalendarField;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

public class CalendarDemo extends BorderPage {

    public CalendarDemo() {
        Form form = new Form("form");
        addControl(form);

        form.add(new TextField("firstname"));
        form.add(new TextField("lastname"));
        form.add(new UICalendarField("calendar"));

        form.add(new Submit("submit"));
    }
}

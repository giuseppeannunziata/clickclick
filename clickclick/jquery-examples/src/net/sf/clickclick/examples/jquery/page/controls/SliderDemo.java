package net.sf.clickclick.examples.jquery.page.controls;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ui.UISliderField;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;

public class SliderDemo extends BorderPage {

    public SliderDemo() {
        Form form = new Form("form");
        addControl(form);

        form.add(new TextField("firstname"));
        form.add(new TextField("lastname"));
        HiddenField ageField = new HiddenField("age", Integer.class);
        form.add(ageField);
        form.add(new UISliderField("slider"));

        form.add(new Submit("submit"));
    }
}

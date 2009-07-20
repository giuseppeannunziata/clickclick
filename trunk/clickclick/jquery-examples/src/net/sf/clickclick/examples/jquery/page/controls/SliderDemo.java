package net.sf.clickclick.examples.jquery.page.controls;

import java.util.HashMap;
import java.util.List;
import net.sf.clickclick.examples.jquery.control.ui.UISliderField;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.element.JsScript;

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

     public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Add calendar-demo.js script
            headElements.add(new JsScript("/controls/slider-demo.js", new HashMap()));
        }
        return headElements;
    }
}

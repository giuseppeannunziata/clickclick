package net.sf.clickclick.examples.jquery.page.controls;

import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.colorpicker.JQColorPicker;

public class ColorPickerPage extends BorderPage {

    public String title = "Color Picker Demo";

    private Form form = new Form("form");

    public void onInit() {
        JQColorPicker colorPicker = new JQColorPicker("colorPicker");
        colorPicker.setValue("a61b1b");
        form.add(colorPicker);
        
        form.add(new Submit("submit"));
        addControl(form);
    }
}

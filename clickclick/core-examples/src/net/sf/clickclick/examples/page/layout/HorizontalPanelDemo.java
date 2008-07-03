package net.sf.clickclick.examples.page.layout;

import net.sf.click.control.BasicForm;
import net.sf.click.control.Checkbox;
import net.sf.click.control.Field;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.IntegerField;
import net.sf.clickclick.control.FieldLabel;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.panel.HorizontalPanel;
import net.sf.clickclick.examples.page.BorderPage;

public class HorizontalPanelDemo extends BorderPage {

    private BasicForm form = new BasicForm("form");

    public void onInit() {
        createLayoutDemo();
        createLayoutWithForm();
    }

    /**
     * Demo 1
     */      
    private void createLayoutDemo() {
        HorizontalPanel horizontalPanel = new HorizontalPanel("demo1");

        Div div = new Div();
        // Use normal CSS properties to style the divs
        div.setStyle("background", "red");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        horizontalPanel.add(div);
        
        div = new Div();
        div.setStyle("background", "yellow");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        horizontalPanel.add(div);

        div = new Div();
        div.setStyle("background", "blue");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");

        horizontalPanel.add(div);
        
        addControl(horizontalPanel);
    }

    /**
     * Demo 2
     */      
    private void createLayoutWithForm() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        form.add(horizontalPanel);

        addField(new TextField("name"), horizontalPanel);
        addField(new IntegerField("age"), horizontalPanel);
        addField(new Checkbox("married"), horizontalPanel);

        addControl(form);
    }

    private void addField(Field field, HorizontalPanel horizontalPanel) {
        FieldLabel label = new FieldLabel(field);
        horizontalPanel.add(label);
        horizontalPanel.add(field);
    }
}

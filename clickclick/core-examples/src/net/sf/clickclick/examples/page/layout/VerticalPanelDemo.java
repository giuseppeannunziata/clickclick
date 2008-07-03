package net.sf.clickclick.examples.page.layout;

import net.sf.click.control.AbstractControl;
import net.sf.click.control.BasicForm;
import net.sf.click.control.Checkbox;
import net.sf.click.control.Field;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.IntegerField;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.FieldLabel;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.Span;
import net.sf.clickclick.control.panel.VerticalPanel;
import net.sf.clickclick.examples.page.BorderPage;

public class VerticalPanelDemo extends BorderPage {

    private BasicForm form = new BasicForm("form");

    public void onInit() {
        createLayoutDemo();
        createLayoutWithForm();
    }

    /**
     * Demo 1
     */
    private void createLayoutDemo() {
        VerticalPanel verticalPanel = new VerticalPanel("demo1");

        Div div = new Div();
        // Use normal CSS properties to style the divs
        div.setStyle("background", "red");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        verticalPanel.add(div);
        
        div = new Div();
        div.setStyle("background", "yellow");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        verticalPanel.add(div);

        div = new Div();
        div.setStyle("background", "blue");
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");

        verticalPanel.add(div);
        
        addControl(verticalPanel);
    }

    /**
     * Demo 2
     */      
    private void createLayoutWithForm() {
        addControl(form);

        VerticalPanel verticalPanel = new VerticalPanel();
        form.add(verticalPanel);

        addField(new TextField("name"), verticalPanel);
        addField(new IntegerField("age"), verticalPanel);
        addField(new Checkbox("married"), verticalPanel);
    }

    private void addField(Field field, VerticalPanel verticalPanel) {
        FieldLabel label = new FieldLabel(field);
        verticalPanel.add(new FieldLabelCombo(field, label));
    }

    class FieldLabelCombo extends AbstractControl {
        private Field field;
        private FieldLabel label;
        public FieldLabelCombo (Field field, FieldLabel label) {
            this.field = field;
            this.label = label;
        }
        public void render (HtmlStringBuffer buffer) {
            Span span = new Span();
            // Float the label to the left with and set a width of 100 pixels
            span.setAttribute("style", "width:100px;display:block;float:left");
            span.add(label);
            span.render(buffer);

            span = new Span();
            // Float the field to the right
            span.setAttribute("style", "display:block;float:right");
            span.add(field);
            span.render(buffer);
        }
    }
}

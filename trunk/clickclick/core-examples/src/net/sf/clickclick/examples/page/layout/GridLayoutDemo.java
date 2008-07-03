package net.sf.clickclick.examples.page.layout;

import net.sf.click.ActionListener;
import net.sf.click.Control;
import net.sf.click.MockContext;
import net.sf.click.control.BasicForm;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;
import net.sf.clickclick.control.grid.Grid;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.examples.page.BorderPage;

public class GridLayoutDemo extends BorderPage {

    private BasicForm form;

    public void onInit() {
        createLayoutDemo();
        createLayoutWithForm();
    }

    /**
     * Demo 1
     */
    private void createLayoutDemo() {
        Grid grid = new Grid("demo1");

        grid.insert(createDiv("red"), 1, 1);
        grid.insert(createDiv("blue"), 2, 1);
        grid.insert(createDiv("yellow"), 3, 1);

        grid.insert(createDiv("yellow"), 1, 2);
        grid.insert(createDiv("red"), 2, 2);
        grid.insert(createDiv("blue"), 3, 2);

        grid.insert(createDiv("blue"), 1, 3);
        grid.insert(createDiv("yellow"), 2, 3);
        grid.insert(createDiv("red"), 3, 3);

        addControl(grid);
    }

    private Div createDiv(String color) {
        Div div = new Div();
        // Use normal CSS properties to style the divs
        div.setStyle("background", color);
        div.setStyle("width", "100px");
        div.setStyle("height", "100px");
        return div;
    }

    /**
     * Demo 2
     */      
    private void createLayoutWithForm() {
        form = new BasicForm("form");
        addControl(form);

        Grid grid = new Grid("grid");
        grid.setAttribute("border", "1");
        grid.setAttribute("cellspacing", "0");
        grid.setAttribute("cellpadding", "0");
        form.add(grid);
        Submit submit = new Submit("submit");
        submit.setActionListener(new ActionListener() {

            public boolean onAction(Control source) {
                if (form.isValid()) {
                   // Save form to database
                }
                return true;
            }            
        });
        form.add(submit);
        
        // Create a 3x3 spreadsheet
        int numRows = 3;
        int numColumns = 3;
        for (int row = 1; row <= numRows; row++) {
            for (int column = 1; column <= numColumns; column++) {
                // Each textField will have a name based on its position in the grid.
                String name = Integer.toString(row) + "_" + Integer.toString(column);
                grid.insert(new TextField(name), row, column);
            }
        }
    }

    /**
     * Internal test
     */
    public static void main(String[] args) {
        MockContext.initContext();
        GridLayoutDemo demo = new GridLayoutDemo();
        demo.onInit();
        System.out.println(demo.form);
    }
}
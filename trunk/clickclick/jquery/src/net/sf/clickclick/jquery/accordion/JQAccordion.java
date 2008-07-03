package net.sf.clickclick.jquery.accordion;

import net.sf.click.Control;
import net.sf.click.MockContext;
import net.sf.click.control.AbstractContainer;
import net.sf.click.control.TextField;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;

/**
 *
 * @author Bob Schellink
 */
public class JQAccordion extends AbstractContainer {

    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_CENTER = "center";
    public static final String ALIGN_RIGHT = "right";

    public static final String ALIGN_TOP = "top";
    public static final String ALIGN_MIDDLE = "middle";
    public static final String ALIGN_BOTTOM = "bottom";

    private String verticalAlignment = ALIGN_TOP;
    
    private String horizontalAlignment = ALIGN_LEFT;

    private HtmlTable table = new HtmlTable();

    public JQAccordion() {
        super.insert(table, 0);
    }

    public Control add(Control control) {
        add(control, null);
        return control;
    }

    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by StackPanel");
    }

    public Control add(Control control, String controlText) {
        Row headerRow = new Row();
        table.add(headerRow);
        Cell headerCell = new Cell();
        headerRow.add(headerCell);
        if (controlText != null) {
            headerCell.setText(controlText);
        }

        Row row = new Row();
        table.add(row);
        Cell cell = new Cell();
        row.add(cell);
        cell.add(control);
        return control;
    }

    public void setHorizontalAlignment(String horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public String getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String verticalAlignment() {
        return verticalAlignment;
    }

    public Row getRowFor(Control control) {
        Cell cell = getCellFor(control);
        return (Row) cell.getParent();
    }

    public Cell getCellFor(Control control) {
        return (Cell) control.getParent();
    }

    public static void main(String[] args) {
        MockContext.initContext();
        JQAccordion panel = new JQAccordion();
        Control field = (Control) panel.add(new TextField("text"), "Some Text");
        Cell cell = panel.getCellFor(field);
        cell.setAttribute("class", "cell");
        System.out.println(panel);
    }
}

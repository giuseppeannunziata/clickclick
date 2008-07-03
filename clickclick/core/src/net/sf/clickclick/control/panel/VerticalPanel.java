package net.sf.clickclick.control.panel;

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
public class VerticalPanel extends AbstractContainer {

    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_CENTER = "center";
    public static final String ALIGN_RIGHT = "right";

    public static final String ALIGN_TOP = "top";
    public static final String ALIGN_MIDDLE = "middle";
    public static final String ALIGN_BOTTOM = "bottom";

    private String verticalAlignment = ALIGN_TOP;
    
    private String horizontalAlignment = ALIGN_LEFT;

    private HtmlTable table = new HtmlTable();

    public VerticalPanel() {
        super.insert(table, 0);
    }

    public VerticalPanel(String name) {
        super(name);
        super.insert(table, 0);
    }

    public Control add(Control control) {
        Row row = new Row();
        table.add(row);
        Cell cell = new Cell();
        cell.setStyle("text-align", getHorizontalAlignment());
        cell.setStyle("vertical-align", getVerticalAlignment());
        row.add(cell);
        cell.add(control);
        return control;
    }

    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by VerticalPanel");
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

    public String getVerticalAlignment() {
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
        VerticalPanel panel = new VerticalPanel();
        Control field = (Control) panel.add(new TextField("text"));
        field = (Control) panel.add(new TextField("text"));
        Cell cell = panel.getCellFor(field);
        cell.setAttribute("class", "cell");
        System.out.println(panel);
    }
}

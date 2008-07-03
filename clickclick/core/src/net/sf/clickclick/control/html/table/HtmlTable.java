package net.sf.clickclick.control.html.table;

import net.sf.click.MockContext;
import net.sf.click.control.AbstractContainer;
import net.sf.click.control.Container;
import net.sf.click.control.HiddenField;
import net.sf.clickclick.control.Html;

/**
 *
 * @author Bob Schellink
 */
public class HtmlTable extends AbstractContainer {

    public HtmlTable() {
    }

    public HtmlTable(String name) {
        super(name);
    }

    public String getTag() {
        return "table";
    }

    public static void main(String[] args) {

        MockContext.initContext();

        HtmlTable table = new HtmlTable("table");
        Row row = new Row("Row1");
        table.add(row);
        row = new Row("Row2");
        table.add(row);
        row = new Row("Row3");
        table.add(row);
        row = new Row("Row4");
        table.add(row);
        System.out.println("===================================START");
        System.out.println(table);
        System.out.println("===================================END");
        table.remove(row);
        System.out.println(table);
        System.out.println("===================================REMOVED");
        
        Container caption = new AbstractContainer() {

            public String getTag() {
                return "caption";
            }
        };
        table.add(caption);

        Html html = new Html("caption 1\n");
        caption.add(html);
        Container thead = new AbstractContainer() {

            public String getTag() {
                return "thread";
            }
        };
        row = new Row();
        thead.add(row);
        Cell cell = new Cell();
        row.add(cell);
        cell = new Cell();
        row.add(cell);
        table.add(thead);

        row = new Row();

        cell = new Cell();
        //cell.setValue("hello");
        html = new Html("hello");
        cell.add(html);
        HiddenField field = new HiddenField("myfield", String.class);
        field.setValue("my hidden value");
        cell.add(field);
        cell.add(new Html("value 2"));
        row.add(cell);
        table.add(row);
        table.add(row);
//         
        System.out.println(table);
    }
}

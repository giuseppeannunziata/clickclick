package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQActionLink;
import net.sf.clickclick.jquery.util.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Table;

public class TableDemo extends BorderPage {

    private JQActionLink link = new JQActionLink("link", "Load table", "linkId");

    public TableDemo() {
        addControl(link);
        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                Table table = loadTable();

                // Remove table in case it is already loaded
                partial.remove(table);

                // Below we use CSS selectors to remove the Table pagination elements
                partial.remove(".pagelinks, .pagebanner");

                // Append a table after the link
                partial.after(link, table);

                return partial;
            }
        });
    }

    private Table loadTable() {
        Table table = new Table("table");

        // Setup customers table
        table.setClass(Table.CLASS_BLUE2);
        table.setHoverRows(true);

        Column column = new Column("id");
        column.setWidth("50px");
        column.setSortable(false);
        table.addColumn(column);

        column = new Column("name");
        column.setWidth("140px;");
        table.addColumn(column);

        column = new Column("email");
        column.setAutolink(true);
        column.setWidth("230px;");
        table.addColumn(column);

        column = new Column("age");
        column.setTextAlign("center");
        column.setWidth("40px;");
        table.addColumn(column);

        column = new Column("holdings");
        column.setFormat("${0,number,#,##0.00}");
        column.setTextAlign("right");
        column.setWidth("100px;");
        table.addColumn(column);

        table.setRowList(getCustomerService().getCustomers().subList(0, 10));
        return table;
    }
}

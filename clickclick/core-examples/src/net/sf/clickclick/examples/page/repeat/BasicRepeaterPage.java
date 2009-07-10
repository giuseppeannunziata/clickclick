package net.sf.clickclick.examples.page.repeat;

import java.util.List;
import net.sf.clickclick.control.html.table.HeaderRow;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.page.BorderPage;

/**
 *
 */
public class BasicRepeaterPage extends BorderPage {

    private Repeater repeater;

    public void onInit() {

        final HtmlTable table = new HtmlTable();
        table.setAttribute("class", "gray");
        table.setBorder(0);
        HeaderRow header = new HeaderRow();
        table.add(header);

        header.add("Name");
        header.add("Age");
        header.add("Holdings");

        repeater = new Repeater("repeater") {

            @Override
            public void buildRow(Object item, RepeaterRow row, int index) {
                Customer customer = (Customer) item;

                Row tableRow = new Row();
                table.add(tableRow);

                tableRow.add(customer.getName());
                tableRow.add(customer.getAge());
                tableRow.add(getFormat().currency(customer.getHoldings()));

                row.add(table);
            }
        };

        addControl(repeater);
        repeater.setItems(getTopCustomers());
    }

    public List<Customer> getTopCustomers() {
        return getCustomerService().getCustomers().subList(0, 5);
    }
}

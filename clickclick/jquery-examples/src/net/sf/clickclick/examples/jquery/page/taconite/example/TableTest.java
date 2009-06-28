package net.sf.clickclick.examples.jquery.page.taconite.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQActionLink;
import net.sf.clickclick.jquery.util.Command;
import net.sf.clickclick.jquery.util.Taconite;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Bob Schellink
 */
public class TableTest extends BorderPage {

    public JQActionLink link = new JQActionLink("link", "Counter: 0");
    public JQActionLink link2 = new JQActionLink("link2", "Add table row");

    public void onInit() {
        super.onInit();
        link.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(link.getParameter("count"));
                ++count;

                // After the link is added, we can change the href parameters.
                link.setParameter("count", Integer.toString(count));
                link.setLabel("Counter: " + Integer.toString(count));
                partial.replace(link);
                partial.remove(link2);
                partial.after(link, link2);
                Table table = createTable();
                table.setClass(Table.CLASS_COMPLEX);
                //command.add(table);
                partial.remove(table);
                partial.after(link2, table);
                return partial;
            }
        });
        
        link2.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                Command command = new Command(Taconite.AFTER, "tr.even", "<tr><td>WHOO HOOO!!!</td></tr>");
                partial.add(command);
                return partial;
            }
        });
    }

    private static Table createTable() {
        Table table = new Table("table");
        table.setSortable(true);
        table.addColumn(new Column("name"));
        table.setRowList(getUsers());
        return table;
    }

    private static List getUsers() {
        List list = new ArrayList();
        Map map = new HashMap();
        list.add(map);
        map.put("name", "John");
        map = new HashMap();
        list.add(map);
        map.put("name", "Sue");
        return list;
    }
}

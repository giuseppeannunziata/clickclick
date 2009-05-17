package net.sf.clickclick.examples.jquery.page.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.clickclick.control.panel.SimplePanel;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Column;
import org.apache.click.control.Table;

/**
 *
 * @author Bob Schellink
 */
public class LazyLoadDemo extends BorderPage {

    Table table1;
    Table table2;

    public LazyLoadDemo() {
         table1 = new Table("table1");
         table1.setWidth("100%");
         table1.setClass(Table.CLASS_BLUE1);
         table1.setNullifyRowListOnDestroy(false);
         table1.addColumn(new Column("firstname"));
         table1.addColumn(new Column("lastname"));
         table1.addColumn(new Column("age"));

         table2 = new Table("table2");
         table2.setWidth("100%");
         table2.setClass(Table.CLASS_BLUE1);
         table2.setNullifyRowListOnDestroy(false);
         table2.addColumn(new Column("firstname"));
         table2.addColumn(new Column("lastname"));
         table2.addColumn(new Column("age"));
    }

    public void onInit() {
        SimplePanel panel1 = new SimplePanel("panel1");
        panel1.setActionListener(new AjaxAdapter(){

            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                table1.setRowList(createData());
                taconite.replace(table1);
                return taconite;
            }
        });

        JQHelper helper = new JQHelper(panel1);
        helper.setIndicatorTarget(panel1);
        helper.setEvent(JQHelper.ON_DOMREADY);
        helper.ajaxify();
        panel1.add(table1);
        addControl(panel1);

        SimplePanel panel2 = new SimplePanel("panel2");
        panel2.setActionListener(new AjaxAdapter(){

            public Partial onAjaxAction(Control source) {
                Taconite taconite = new Taconite();
                table2.setRowList(createData());
                taconite.replace(table2);
                return taconite;
            }
        });

        helper = new JQHelper(panel2);
        helper.setEvent(JQHelper.ON_DOMREADY);
        helper.setIndicatorTarget(panel2);
        helper.ajaxify();

        panel2.add(table2);
        addControl(panel2);
    }

    private List createData() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        List list = new ArrayList();
        list.add(createPerson());
        list.add(createPerson());
        return list;
    }

    private Map createPerson() {
        Map map = new HashMap();
        map.put("firstname", "John");
        map.put("lastname", "Smith");
        map.put("age", "30");
        return map;
    }
}

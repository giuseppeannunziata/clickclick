package net.sf.clickclick.examples.page.repeat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HeaderCell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.examples.page.BorderPage;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.IntegerField;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author bob
 */
public class TableRepeaterPage extends BorderPage {

    public ActionLink reset = new ActionLink("reset", this, "reset");

    private Form form = new Form("form");

    public void onInit() {
        // In non-edit mode, render Master during onInit event in order to
        // apply incoming parameters.
        if (!isEditMode()) {
            createMasterView();
        }
        createDetailView();
    }

    public void onRender() {
        // In edit mode, render Master during onRender event in order to
        // see any changes made during editing.
        if (isEditMode()) {
            createMasterView();
        }
    }

    public boolean reset() {
        getContext().setSessionAttribute("items", null);
        setRedirect(TableRepeaterPage.class);
        return false;
    }

    void createMasterView() {

        final HtmlTable table = new HtmlTable("table");
        table.setAttribute("border", "1");

        // Set Header Row
        Row row = new Row();
        row.add(new HeaderCell("id"));
        row.add(new HeaderCell("firstname"));
        row.add(new HeaderCell("lastname"));
        row.add(new HeaderCell("age"));
        row.add(new HeaderCell("action"));
        table.add(row);

        Repeater repeater = new Repeater() {

            public void buildRow(final Object item, final RepeaterRow row, final int index) {
                Map data = (Map) item;
                Row tableRow = new Row();
                tableRow.add(data.get("id"));
                tableRow.add(data.get("firstname"));
                tableRow.add(data.get("lastname"));
                tableRow.add(data.get("age"));

                ActionLink delete = new ActionLink("delete");
                delete.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        removeItem(item);

                        // Perform redirect to guard against user hitting refresh
                        // and setting the ActionLink value to the deleted recordId
                        setRedirect(TableRepeaterPage.class);
                        return false;
                    }
                });
                Cell actions = new Cell();
                tableRow.add(actions);
                actions.add(delete);

                ActionLink edit = new ActionLink("edit");
                edit.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        // Copy the item to edit to the Form. This sets the Page
                        // into edit mode.
                        form.copyFrom(item);
                        return true;
                    }
                });
                actions.add(new Text(" | "));
                actions.add(edit);

                row.add(tableRow);
            }
        };

        table.add(repeater);

        repeater.setItems(getItems());

        addControl(table);
    }

    void createDetailView() {
        // Setup customers form
        FieldSet fieldSet = new FieldSet("customer");
        final HiddenField idField = new HiddenField("id", String.class);
        fieldSet.add(idField);
        fieldSet.add(new TextField("firstname"));
        fieldSet.add(new TextField("lastname"));
        fieldSet.add(new IntegerField("age"));
        form.add(fieldSet);
        Submit submit = new Submit("save");
        submit.setActionListener(new ActionListener() {
            public boolean onAction(Control source) {
                if (form.isValid()) {
                    String id = idField.getValue();
                    Map data = findItem(id);
                    form.copyTo(data);

                    // Perform redirect to ensure the form changes are reflected
                    // by the Repeater
                    // setRedirect(TableRepeaterPage.class);
                }
                return true;
            }
        });
        form.add(submit);
        Submit cancel = new Submit("cancel");
        cancel.setActionListener(new ActionListener(){
            public boolean onAction(Control source) {
                form.clearValues();
                form.clearErrors();
                return true;
            }
        });
        form.add(cancel);
        form.add(new HiddenField(Table.PAGE, String.class));
        form.add(new HiddenField(Table.COLUMN, String.class));

        addControl(form);
    }

    // -------------------------------------------------------- Private Methods

    private Map findItem(String id) {
        List items = getItems();
        Map item = null;
        for (int i = 0; i < items.size(); i++) {
            Map tmp = (Map) items.get(i);
            if (tmp.get("id").equals(id)) {
                item = tmp;
                break;
            }
        }
        return item;
    }

    private List getItems() {
        List items = (List) getContext().getSessionAttribute("items");
        if (items == null) {
            items = createItems();
            getContext().setSessionAttribute("items", items);
        }
        return items;
    }

    private List createItems() {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Map data = new HashMap();
            data.put("firstname", "one");
            data.put("lastname", "two");
            data.put("age", Integer.toString(i));
            data.put("id", Integer.toString(i * 100));
            list.add(data);
        }
        return list;
    }

    private boolean isEditMode() {
        String formName = getContext().getRequestParameter(Form.FORM_NAME);
        return StringUtils.isNotBlank(formName);
    }
}

package net.sf.clickclick.examples.page.repeat;

import java.util.ArrayList;
import java.util.List;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import net.sf.clickclick.control.panel.HorizontalPanel;
import net.sf.clickclick.control.panel.VerticalPanel;
import net.sf.clickclick.control.repeater.RepeaterRow;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.examples.domain.Product;
import org.apache.click.extras.control.SubmitLink;

public class RepeatFormPage extends AbstractRepeatPage {

    public void onInit() {
        super.onInit();

        Form form = new Form("form");

        Submit add = new Submit("add");
        add.setActionListener(new ActionListener() {

            public boolean onAction(Control source) {
                Product product = new Product();
                repeater.addItem(product);
                return true;
            }
        });
        form.add(add);

        addControl(form);

        repeater = new Repeater("repeater") {

            public void buildRow(final Object item, final RepeaterRow row,
                final int index) {

                HorizontalPanel horizontalPanel = new HorizontalPanel();
                VerticalPanel verticalPanel = new VerticalPanel();
                verticalPanel.addStyleClass("vertical-panel");

                final SubmitLink moveUp = new SubmitLink("up");
                final SubmitLink moveDown = new SubmitLink("down");
                verticalPanel.add(moveUp);
                verticalPanel.add(moveDown);

                Form form = new Form("form");
                row.add(form);

                FieldSet fieldSet = new FieldSet("product");
                form.add(horizontalPanel);
                horizontalPanel.add(fieldSet);
                horizontalPanel.add(verticalPanel);

                fieldSet.add(new TextField("name")).setRequired(true);

                Submit save = new Submit("save");
                save.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        return onSubmit(item, index);
                    }
                });
                fieldSet.add(save);

                Submit insert = new Submit("insert");
                insert.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        Product product = new Product();
                        repeater.insertItem(product, index);
                        return true;
                    }
                });
                fieldSet.add(insert);

                Submit delete = new Submit("delete");
                delete.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        repeater.removeItem(item);
                        return true;
                    }
                });
                fieldSet.add(delete);

                moveUp.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        repeater.moveUp(item);
                        return true;
                    }
                });

                moveDown.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        repeater.moveDown(item);
                        return true;
                    }
                });

                form.copyFrom(item);
            }
        };
        repeater.setItems(getProducts());

        addControl(repeater);
    }

    public boolean onSubmit(Object item, int index) {
        List products = getProducts();
        RepeaterRow row = (RepeaterRow) repeater.getControls().get(index);
        Form form = (Form) row.getControl("form");
        if (form.isValid()) {
            System.out.println("**** Form valid **** ");
            repeater.copyTo(item);
            System.out.println("Product after copy -> " + products.get(index));
        } else {
            System.out.println("WARNING: Form invalid");
        }
        return true;
    }

    public void onRender() {
        toggleLinks(getProducts().size());
    }

    // -------------------------------------------------------- Private Methods

    private List getProducts() {
        List products = (List) getContext().getSessionAttribute("products");
        if (products == null) {
            products = createProducts();
            getContext().setSessionAttribute("products", products);
        }
        return products;
    }

    private List createProducts() {
        List list = new ArrayList();
        list.add(new Product("Ham"));
        list.add(new Product("Cheese"));
        list.add(new Product("Meat"));
        return list;
    }

}

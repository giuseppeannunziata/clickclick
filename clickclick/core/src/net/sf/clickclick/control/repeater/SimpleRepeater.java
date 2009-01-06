package net.sf.clickclick.control.repeater;

import java.util.List;
import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.Container;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Panel;
import org.apache.click.util.ContainerUtils;

/**
 * This control does the following:
 * Include child controls in its namespace by prefixing control names with its own
 *
 * @author Bob Schellink
 */
public abstract class SimpleRepeater extends AbstractContainer {

    private static final long serialVersionUID = 1L;

    protected List items = null;

    public SimpleRepeater() {
    }
    
    public SimpleRepeater(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Get the value of items.
     *
     * @return the value of items
     */
    public List getItems() {
        return items;
    }

    /**
     * Set the value of items and delegates to {@link #buildRows()} to start
     * building the repeater controls.
     *
     * @param items new value of items
     */
    public void setItems(List items) {
        this.items = items;
        buildRows();
    }

    // --------------------------------------------------------- Public Methods

    public Control add(Control control) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public void insertItem(Object item, int index) {
        getItems().add(index, item);
        createRow(index);
    }

    public void addItem(Object item) {
        getItems().add(item);
        int index = getItems().size();
        createRow(index - 1);
    }

    public void removeItem(Object item) {
        int index = getItems().indexOf(item);
        boolean removed = getItems().remove(item);
        if (removed) {
            removed = removeRow(index);
            // TODO throw exception if row was not removed?
        }
    }

    public void moveUp(Object item) {
        List items = getItems();
        int index = items.indexOf(item);

 
        // If item is already at top, exit early
        if (index == 0) {
            return;
        }

        boolean itemRemoved = items.remove(item);
        RepeaterRow row = (RepeaterRow) getControls().get(index);
        boolean rowRemoved = super.remove(row);

        // If item or row was not removed, exist early
        if (!itemRemoved || !rowRemoved) {
            return;
        }
        // Decrement index and add item and row at new index
        --index;
        items.add(index, item);
        super.insert(row, index);
    }

    public void moveDown(Object item) {
        List items = getItems();
        int index = items.indexOf(item);

        
        // If item is already at bottom, exit early
        if (index == items.size() - 1) {
            return;
        }

        boolean itemRemoved = items.remove(item);
        RepeaterRow row = (RepeaterRow) getControls().get(index);
        boolean rowRemoved = super.remove(row);

        // If item or row was not removed, exist early
        if (!itemRemoved || !rowRemoved) {
            return;
        }

        // Increment index and add item and row at new index
        ++index;
        items.add(index, item);
        super.insert(row, index);
    }

    // TODO create actionLinks for moveUp and moveDown ???

    protected void buildRows() {
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); i++ ) {
            createRow(i);
        }
        super.onInit();
    }
        
    public abstract void buildRow(Object item, RepeaterRow row, int index);

    public boolean onProcess() {
        // Update control name indexes to match incoming request parameters
        addIndexToControlNames(false);
        boolean result = super.onProcess();
        
        // Unwind control name indexes here so that new RepeaterRows added or
        // inserted after onProcess (e.g. listener callbacks) won't end up with
        // indexes which is out of order.
        removeIndexFromControlNames();
        return result;
    }
    
    public void onRender() {
        // TODO onRender has no guarantee that it will be called as user can
        // cancel this phase by returning false from listener.
        super.onRender();
        
        // Before rendering update control name indexes so that each control
        // will have a unique request parameter when posting to the server
        addIndexToControlNames(true);
    }

    public void copyTo() {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        List items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            copyTo(item);
        }
    }
    
    public void copyFrom() {
        List items = getItems();
        if (items == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            copyFrom(item);
        }
    }
    
    public void copyTo(Object item) {
        List items = getItems();
        if (items == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        //Object object = getItems().get(index);
        int index = items.indexOf(item);
        Container container = (Container) getControls().get(index);
        ContainerUtils.copyContainerToObject(container, item);
    }

    
    public void copyFrom(Object item) {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        //Object object = getItems().get(index);
        int index = getItems().indexOf(item);
        Container container = (Container) getControls().get(index);
        ContainerUtils.copyObjectToContainer(item, container);
    }

    // ------------------------------------------------------ Protected Methods

    protected void addIndexToControlNames(boolean updateLabels) {
        List controls = getControls();
        for (int count = 0; count < controls.size(); count++ ) {
            RepeaterRow container = (RepeaterRow) controls.get(count);
            addIndexToControlNames(container, String.valueOf(count), updateLabels);
        }
    }

    protected void removeIndexFromControlNames() {
        List controls = getControls();
        for (int count = 0; count < controls.size(); count++ ) {
            RepeaterRow container = (RepeaterRow) controls.get(count);
            removeIndexFromControlNames(container);
        }
    }
    
    // -------------------------------------------------------- Private Methods

    private void addIndexToControlNames(final Container container, String index, boolean updateLabels) {
        List controls = container.getControls();
        for (int i = 0; i < controls.size(); i++) {
            Control control = (Control) controls.get(i);
            if (control instanceof Field) {
                Field field = (Field) control;

                // Ensure the field label gets cached in case its name is changed
                field.getLabel();
                if (control instanceof FieldSet) {
                    // Ensure fieldSet legend gets cached in case its name is changed
                    ((FieldSet) field).getLegend();
                }
            } else if (control instanceof AbstractLink) {
                ((AbstractLink) control).getLabel();
            } else if (control instanceof Panel) {
                ((Panel) control).getLabel();
            }
            ammendName(control, index);
            if (control instanceof Container) {
               Container childContainer = (Container) control;
               addIndexToControlNames(childContainer, index, updateLabels);
            }
        }
    }

    private void removeIndexFromControlNames(final Container container) {
        List controls = container.getControls();
        for (int i = 0; i < controls.size(); i++) {
            Control control = (Control) controls.get(i);
            revertName(control);
            if (control instanceof Container) {
               Container childContainer = (Container) control;
               removeIndexFromControlNames(childContainer);
            }
        }
    }

    private void ammendName(Control control, String id) {
        if (control.getName() == null) {
            return;
        }
        String indexedName = control.getName() + "_" + id;
        control.setName(indexedName);
    }

    private void revertName(Control control) {
        if (control.getName() == null) {
           return;
        }
        int index = control.getName().lastIndexOf("_");
        if (index >= 0) {
            control.setName(control.getName().substring(0, index));
        }
    }

    private void createRow(int index) {
        RepeaterRow row = new RepeaterRow();
        super.insert(row, index);
        Object item = getItems().get(index);
        buildRow(item, row, index);
    }

    private boolean removeRow(int index) {
        RepeaterRow row = (RepeaterRow) getControls().get(index);
        return super.remove(row);
    }
}

/*
 * Copyright 2008 Bob Schellink
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.control.repeater;

import java.util.ArrayList;
import java.util.List;
import org.apache.click.ActionListener;
import org.apache.click.Control;
import org.apache.click.MockContext;
import org.apache.click.control.AbstractContainer;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Container;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Panel;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.ContainerUtils;

/**
 * This control does the following:
 * Include child controls in its namespace by prefixing control names with its own
 * 
 * @author Bob Schellink
 */
public abstract class Repeater extends AbstractContainer {

    private static final long serialVersionUID = 1L;

    protected List items = null;

    public Repeater() {
    }

    public Repeater(String name) {
        super(name);
    }

    /**
     * Get the value of items.
     *
     * @return the value of items
     */
    public List getItems() {
        return items;
    }

    /**
     * Set the value of items.
     *
     * @param items new value of items
     */
    public void setItems(List items) {
        this.items = items;
    }

    public void onInit() {
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); i++ ) {
            newItem(i);
        }
        super.onInit();
    }

    public abstract void onInit(Object item, Container container, int index);

    public void newItem(int index) {
        RepeaterRow container = new RepeaterRow();
        super.insert(container, index);
        Object item = getItems().get(index);
        onInit(item, container, index);
    }

    public void insertItem(Container container, int index) {
        if (!(container instanceof RepeaterRow)) {
            throw new RuntimeException("Only RepeaterContainer can be inserted.");
        }
        super.insert(container, index);
    }

    public Control insert(Control control, int index) {
        throw new RuntimeException("You cannot use insert directly on a Repeater."
            + " Add components to the repeater by implementing the method"
            + " onInit(Object, Container, int");
    }

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

    public void copyToItems() {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        List items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            Container container = (Container) getControls().get(i);
            ContainerUtils.copyContainerToObject(container, object);
        }
    }
    
    public void copyToItem(int index) {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        Object object = getItems().get(index);
        Container container = (Container) getControls().get(index);
        ContainerUtils.copyContainerToObject(container, object);
    }

    public void copyFromItems() {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        List items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            Container container = (Container) getControls().get(i);
            ContainerUtils.copyObjectToContainer(object, container);
        }
    }
    
    public void copyFromItem(int index) {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        Object object = getItems().get(index);
        Container container = (Container) getControls().get(index);
        ContainerUtils.copyObjectToContainer(object, container);
    }

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
        String indexedName = control.getName() + "_" + id;
        control.setName(indexedName);
    }

    private void revertName(Control control) {
        int index = control.getName().lastIndexOf("_");
        if (index >= 0) {
            control.setName(control.getName().substring(0, index));
        }
    }

    public static void main(String[] args) {
        MockContext context = MockContext.initContext();
        context.getMockRequest().setParameter("innerField_0", "mooInner");
        context.getMockRequest().setParameter("outerField_0", "mooOuter");
        context.getMockRequest().setParameter("innerField_1", "poooo");
        context.getMockRequest().setParameter("add_1", "ok");
        context.getMockRequest().setParameter("delete_0", "ok");
        
        List items = new ArrayList();
        items.add("one");
        items.add("two");

        final Repeater repeater = new Repeater("repeater") {
            public void onInit(final Object item, final Container container, final int index) {
                String value = item.toString();
                AbstractContainer ac = new AbstractContainer("container") {};
                container.add(ac);
                ac.add(new TextField("innerField"));
                ActionLink link = new ActionLink("link", "Add Link");
                link.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        System.out.println("Link clicked");
                        return true;
                    }
                });
                ac.add(link);
                TextField outerField = new TextField("outerField", "Outer Field");
                outerField.setRequired(true);
                container.add(outerField);
                Submit add = new Submit("add");
                add.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        System.out.println("Adding");
                        getItems().add(1, "three");
                        newItem(1);
                        return true;
                    }                    
                });
                container.add(add);
                Submit delete = new Submit("delete");
                delete.setActionListener(new ActionListener() {
                    public boolean onAction(Control source) {
                        System.out.println("Deleting");
                        items.remove(item);
                        remove(container);
                        return true;
                    }
                });
                container.add(delete);
            }
        };

        repeater.setItems(items);

        repeater.onInit();
        repeater.onProcess();
        context.fireActionEventsAndClearRegistry();
        repeater.onRender();
        Container container = (Container) repeater.getControls().get(0);
        TextField field = (TextField) container.getControl("outerField");
        System.out.println("WTF " + field + " Required -> " + field.isRequired());
        System.out.println(field.getError());

        System.out.println(repeater);
        
        System.out.println(items.size());
    }
}

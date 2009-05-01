/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.clickclick.control.repeater;

import java.util.List;
import org.apache.click.control.Container;

import org.apache.click.control.Field;
import org.apache.click.util.ContainerUtils;

/**
 *
 * @author bob
 */
public abstract class FieldRepeater extends Repeater {

    private String fieldName;

    public FieldRepeater(String name, String fieldName) {
        super(name);
        this.fieldName = fieldName;
    }

    public FieldRepeater(String name) {
        super(name);
    }

    public FieldRepeater() {
    }

    /**
     * By default copyToItems will update the given item with the values of
     * any Field found in the repeater.
     * However in this case item is an immutable String, thus instead of
     * updating the item, it is simply replaced.
     * <p/>
     * By default this method assumes a Field will be the target Control to copy
     * the String item from. You can override this method if a different Control
     * should be used to copy the String item from. For example:
     * <pre class="prettyprint">
     * public void copyTo(Object item) {
     *     items.set(index, actionLink.getValue());
     * } </pre>
     */
    public void copyTo(Object item) {
        List items = getItems();
        if (items == null) {
            throw new IllegalStateException("Items cannot be null.");
        }
        int index = items.indexOf(item);

        Container container = (Container) getControls().get(index);

        // Find the first Field in the repeater
        Field field = (Field) ContainerUtils.findControlByName(container, getFieldName());

        if (field == null) {
            return;
        }

        // String is immutable so simply replace the items at the item's
        // index
        items.set(index, field.getValueObject());
    }

    /**
     * By default copyFromItems will update the values of any Field found in
     * the repeater with matching properties of the given item.
     * However in this case item is an immutable String, thus instead of
     * updating the field, its value is simply replaced with the item.
     * <p/>
     * By default this method assumes a Field will be the target Control to copy
     * the String item to. You can override this method if a different Control
     * should be used to the String item to. For example:
     * <pre class="prettyprint">
     * public void copyTo(Object item) {
     *     actionLink.setValueObject(item);
     * }
     * </pre>
     */
    public void copyFrom(Object item) {
        List items = getItems();
        if (items == null) {
            throw new IllegalStateException("Items cannot be null.");
        }
        int index = items.indexOf(item);
        Container container = (Container) getControls().get(index);

        // Find the first Field in the repeater
        Field field = (Field) ContainerUtils.findControlByName(container, getFieldName());

        if (field == null) {
            return;
        }

        // String is immutable so simply set the field value to that of
        // the item
        field.setValueObject(item);
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

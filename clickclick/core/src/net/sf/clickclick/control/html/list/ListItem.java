package net.sf.clickclick.control.html.list;

import org.apache.click.control.AbstractContainer;

/**
 * This control provides HTML listitem for ordered and unordered lists.
 *
 * @author Bob Schellink
 */
public class ListItem extends AbstractContainer {

    public ListItem() {
    }

    public ListItem(String name) {
        if(name != null) {
            setName(name);
        }
    }

    public ListItem(String name, String id) {
        this(name);
        setAttribute("id", id);
    }

    public final String getTag() {
        return "li";
    }
}

package net.sf.clickclick.control.html.list;

import org.apache.click.control.AbstractContainer;

/**
 * Provides an HTML ListItem control: &lt;li&gt;.
 *
 * @author Bob Schellink
 */
public class ListItem extends AbstractContainer {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default ListItem.
     */
    public ListItem() {
    }

    /**
     * Create a ListItem with the given name.
     *
     * @param name the name of the ListItem
     */
    public ListItem(String name) {
        if(name != null) {
            setName(name);
        }
    }

    /**
     * Create a ListItem with the given name and id.
     *
     * @param name the name of the ListItem
     * @param id the id of the ListItem
     */
    public ListItem(String name, String id) {
        this(name);
        setAttribute("id", id);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the ListItem html tag: <tt>li</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public final String getTag() {
        return "li";
    }
}

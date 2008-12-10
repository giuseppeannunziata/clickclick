package net.sf.clickclick.control.menu;

import java.text.MessageFormat;
import java.util.Iterator;
import net.sf.click.extras.control.Menu;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;

/**
 * FlexiMenu overrides setContext (), so that the menu can be built programmatically
 * without the need of a menu.xml configuration.
 */
public class FlexiMenu extends Menu {

    public static final String HORIZONTAL = "horizontal";

    public static final String VERTICAL = "vertical";

    private String orientation = VERTICAL;

    protected static final String MENU_IMPORTS =
        "<link type=\"text/css\" rel=\"stylesheet\" href=\"{0}/clickclick/core/menu/{2}-menu.css\"></link>\n" +
        "<script type=\"text/javascript\" src=\"{0}/click/control{1}.js\"></script>\n" +
        "<script type=\"text/javascript\" src=\"{0}/clickclick/core/menu/menu.js\"></script>\n" +
        "<script type=\"text/javascript\">addLoadEvent( function() '{ initMenu() '} );</script>\n";

    /**
     * Call super default constructor
     */
    public FlexiMenu() {
        super();
    }

    /**
     * Create a new root Menu instance with the given name.
     *
     * @param name the name of the menu
     */
    public FlexiMenu(String name) {
        super(name);
    }

    public void add(Menu menu) {
        getChildren().add(menu);
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean hasChildren() {
        if (getChildren().size() == 0) {
            return false;
        }
        return true;
    }

    public String getHref() {
        if (getPath() == null) {
            setPath("#");
        } else if (hasChildren() && "".equals(getPath())) {
            setPath("#");
        }
        if (isExternal()) {
            return getPath();

        } else if ("#".equals(getPath())) {
            return getContext().getResponse().encodeURL(getPath());

        } else {
            return getContext().getResponse().encodeURL(getContext().getRequest().
                getContextPath() + "/" + getPath());
        }
    }

    public boolean isUserInChildMenuRoles() {
        if (getContext().getRequest().getRemoteUser() == null) {
            return true;
        }
        return super.isUserInChildMenuRoles();
    }

    public boolean isUserInRoles() {
        if (getContext().getRequest().getRemoteUser() == null) {
            return true;
        }
        return super.isUserInRoles();
    }

    public String getHtmlImports() {
        String[] args = {
            getContext().getRequest().getContextPath(),
            ClickUtils.getResourceVersionIndicator(getContext()),
            getOrientation()
        };

        return MessageFormat.format(MENU_IMPORTS, args);
    }

    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("ul");
        buffer.appendAttribute("class", "menu");
        buffer.appendAttribute("id", getOrientation() + "Menu");
        buffer.closeTag();
        buffer.append("\n");
        renderMenu(buffer, this);
        buffer.elementEnd("ul");
    }

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(256);
        render(buffer);
        return buffer.toString();
    }

    protected void renderMenu(HtmlStringBuffer buffer, Menu menu) {
        Iterator it = menu.getChildren().iterator();
        while (it.hasNext()) {
            Menu child = (Menu) it.next();
            if (child.isUserInRoles()) {
                buffer.elementStart("li");
                HtmlStringBuffer sb = new HtmlStringBuffer().append("menuItem");
                if (child.getChildren().size() == 0) {
                    buffer.appendAttribute("class", sb.toString());
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuItem(buffer, child);
                } else {
                    sb.append(" ").append("menuItemBullet");
                    buffer.appendAttribute("class", sb.toString());
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuItem(buffer, child);
                    buffer.elementStart("ul");
                    buffer.appendAttribute("class", "submenu");
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenu(buffer, child);
                    buffer.elementEnd("ul");
                    buffer.append("\n");
                }
                buffer.elementEnd("li");
                buffer.append("\n");
            }
        }
    }

    protected void renderMenuItem(HtmlStringBuffer buffer, Menu menu) {

        buffer.elementStart("a");

        String href = menu.getHref();
        buffer.appendAttribute("href", href);
        if (menu.hasAttributes()) {
            buffer.appendAttributes(menu.getAttributes());
        }
        if ("#".equals(href)) {
            //If hyperlink does not return false here, clicking on it will scroll
            //to the top of the page.
            buffer.appendAttribute("onclick", "return false;");
        }
        buffer.appendAttribute("title", menu.getTitle());
        buffer.closeTag();
        buffer.append("\n");
        buffer.append(menu.getLabel());
        buffer.elementEnd("a");
        buffer.append("\n");
    }
}

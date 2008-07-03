package net.sf.clickclick.control.menu;

import java.text.MessageFormat;
import java.util.Iterator;
import javax.servlet.ServletContext;
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

    /** The Menu resource file names. */
    static final String[] MENU_RESOURCES = {
        "/net/sf/clickclick/control/menu/arrow_down.png",
        "/net/sf/clickclick/control/menu/arrow_up.png",
        "/net/sf/clickclick/control/menu/arrow_left.png",
        "/net/sf/clickclick/control/menu/arrow_right.png",
        "/net/sf/clickclick/control/menu/horizontal-menu.css",
        "/net/sf/clickclick/control/menu/vertical-menu.css",
        "/net/sf/clickclick/control/menu/menu.js",
        "/net/sf/clickclick/control/menu/vertical-menu2.css"
    };

    protected static final String MENU_IMPORTS =
        "<link type=\"text/css\" rel=\"stylesheet\" href=\"{0}/click/menu/{2}-menu.css\"></link>\n" +
        "<script type=\"text/javascript\" src=\"{0}/click/control{1}.js\"></script>\n" +
        "<script type=\"text/javascript\" src=\"{0}/click/menu/menu.js\"></script>\n" +
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

    public void onDeploy(ServletContext servletContext) {
        ClickUtils.deployFiles(servletContext, MENU_RESOURCES, "click/menu");
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
        renderMenu(buffer, this, 0);
        buffer.elementEnd("ul");
    }

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(256);
        render(buffer);
        return buffer.toString();
    }

    protected void renderMenu(HtmlStringBuffer buffer, Menu menu,
        int indentationLevel) {
        indentationLevel++;
        Iterator it = menu.getChildren().iterator();
        while (it.hasNext()) {
            Menu child = (Menu) it.next();
            if (child.isUserInRoles()) {
                buffer.elementStart("li");
                StringBuffer sb = new StringBuffer("menuItem");
                if (child.getChildren().size() == 0) {
                    buffer.appendAttribute("class", sb.toString());
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuItem(buffer, child, indentationLevel);
                } else {
                    sb.append(" ").append("menuItemBullet");
                    buffer.appendAttribute("class", sb.toString());
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuItem(buffer, child, indentationLevel);
                    buffer.elementStart("ul");
                    buffer.appendAttribute("class", "submenu");
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenu(buffer, child, indentationLevel);
                    buffer.elementEnd("ul");
                    buffer.append("\n");
                }
                buffer.elementEnd("li");
                buffer.append("\n");
            }
        }
    }

    protected void renderMenuItem(HtmlStringBuffer buffer, Menu menu,
        int indentation) {

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

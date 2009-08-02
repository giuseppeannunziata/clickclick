/*
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
package net.sf.clickclick.control.menu;

import java.util.Iterator;
import java.util.List;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a menu with the following features:
 * <ul>
 * <li>FlexiMenu contains two predefined styles to layout the menus either
 * {@link #HORIZONTAL horizontally} or {@link #VERTICAL vertically}.</li>
 * <li>FlexiMenus are constructed programmatically, thus the application menu
 * can be specified in a relational database.</li>
 * </ul>
 */
public class FlexiMenu extends Menu {

    // -------------------------------------------------------------- Constants

    /** The menu CSS style: <tt>"horizontal"</tt>. */
    public static final String HORIZONTAL = "horizontal";

    /** The menu CSS style: <tt>"vertical"</tt>. */
    public static final String VERTICAL = "vertical";

    // -------------------------------------------------------------- Variables

    /** The menu orientation, by default {@link #VERTICAL}. */
    private String orientation = VERTICAL;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default menu.
     */
    public FlexiMenu() {
    }

    /**
     * Create a FlexiMenu with the given name.
     *
     * @param name the name of the menu
     */
    public FlexiMenu(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the menu orientation.
     *
     * @see #setOrientation(java.lang.String)
     *
     * @return the menu orientation
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Set the menu orientation.
     * <p/>
     * Supported values are: {@link #VERTICAL} and {@link #HORIZONTAL}.
     *
     * @param orientation the menu orientation
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * Return true if the menu contains any child submenus.
     *
     * @return true if the menu contains any child submenus
     */
    public boolean hasChildren() {
        if (getChildren().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Return the HTML href attribute value.
     *
     * @return the HTML href attribute value
     */
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

    // --------------------------------------------------------- Public Methods

    /**
     * Add the given menu as a submenu.
     *
     * @param menu the submenu to add
     */
    public void add(Menu menu) {
        getChildren().add(menu);
    }

    /**
     * Return true if any child menus have the user in one of their menu roles,
     * false otherwise.
     * <p/>
     * If no {@link #getRoles() roles} are defined, this method returns true,
     * meaning all users will be able to view all menus.
     *
     * @return true if the user is in one of the child menu roles, or false otherwise
     */
    public boolean isUserInChildMenuRoles() {
        List roles = getRoles();
        if (roles == null || roles.isEmpty()) {
            return true;
        }
        return super.isUserInChildMenuRoles();
    }

    /**
     * Return true if the user is in one of the menu roles, false otherwise.
     * <p/>
     * If no {@link #getRoles() roles} are defined, this method returns true,
     * meaning all users will be able to view all menus.
     *
     * @return true if the user is in one of the menu roles, false otherwise
     */
    public boolean isUserInRoles() {
        List roles = getRoles();
        if (roles == null || roles.isEmpty()) {
            return true;
        }
        return super.isUserInRoles();
    }

    /**
     * Override default implementation and return null.
     *
     * @see #getHeadElements()
     *
     * @return override default implementation and return null
     */
    public String getHtmlImports() {
        return null;
    }

    /**
     * Return the FlexiMenu resources:
     *
     * <ul>
     * <li><tt>/clickclick/core/menu/{orientation}-menu.css</tt> - where
     * {orientation} is the Menu {@link #orientation}: {@link #HORIZONTAL}
     * or {@link #VERTICAL}</li>
     * <li><tt>/click/control.js</tt></li>
     * <li><tt>/clickclick/core/menu/menu.js</tt></li>
     * </ul>
     *
     * @return the list of html imports
     */
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            String menuStyle = getOrientation() + "-menu.css";
            CssImport cssImport = new CssImport("/clickclick/core/menu/" + menuStyle);
            headElements.add(cssImport);

            JsImport jsImport = new JsImport("/click/control.js");
            headElements.add(jsImport);

            jsImport = new JsImport("/clickclick/core/menu/menu.js");
            headElements.add(jsImport);

            JsScript jsScript = new JsScript("addLoadEvent( function() { initMenu() } );");
            headElements.add(jsScript);
        }

        return headElements;
    }

    /**
     * Render the HTML representation of the Menu.
     *
     * @see #toString()
     *
     * @param buffer the specified buffer to render the control's output to
     */
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("ul");
        buffer.appendAttribute("class", "menu");
        buffer.appendAttribute("id", getOrientation() + "Menu");
        buffer.closeTag();
        buffer.append("\n");
        renderMenu(buffer, this);
        buffer.elementEnd("ul");
    }

    /**
     * Render the HTML representation of the Menu.
     *
     * @return the HTML representation of the Menu
     */
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(256);
        render(buffer);
        return buffer.toString();
    }

    /**
     * Render the given menu.
     *
     * @param buffer the buffer to render to
     * @param menu the menu to render
     */
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
                    renderMenuLink(buffer, child);
                } else {
                    sb.append(" ").append("menuItemBullet");
                    buffer.appendAttribute("class", sb.toString());
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuLink(buffer, child);
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

    /**
     * Render the given menu as a link.
     *
     * @param buffer the buffer to render to
     * @param menu the menu to render as a link
     */
    protected void renderMenuLink(HtmlStringBuffer buffer, Menu menu) {

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

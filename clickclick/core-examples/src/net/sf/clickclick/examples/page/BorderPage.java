package net.sf.clickclick.examples.page;

import net.sf.click.Page;
import net.sf.click.extras.control.Menu;
import net.sf.click.util.ClickUtils;
import net.sf.clickclick.control.breadcrumb.Breadcrumb;
import net.sf.clickclick.examples.nav.MenuBuilder;

/**
 *
 * @author Bob Schellink
 */
public class BorderPage extends Page {

    public Menu rootMenu = MenuBuilder.getMenus();

    private Breadcrumb breadcrumb;

    public BorderPage() {
        try {
            String className = getClass().getName();

            String shortName = className.substring(className.lastIndexOf('.') +
                1);
            String title = ClickUtils.toLabel(shortName);
            addModel("title", title);

            breadcrumb = new Breadcrumb("breadcrumb", 4);
            breadcrumb.setSeperator(" | ");
            breadcrumb.getExcludedPaths().add("login");
            addControl(breadcrumb);
            setTemplate("border-template.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTemplate() {
        return "/border-template.htm";
    }
}

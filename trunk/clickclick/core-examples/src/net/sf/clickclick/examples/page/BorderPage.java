package net.sf.clickclick.examples.page;

import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.ClickUtils;
import net.sf.clickclick.control.breadcrumb.Breadcrumb;
import net.sf.clickclick.examples.nav.MenuBuilder;
import net.sf.clickclick.examples.services.ApplicationRegistry;
import net.sf.clickclick.examples.services.CustomerService;

/**
 *
 */
public class BorderPage extends Page {

    public Menu rootMenu = MenuBuilder.getMenus();

    private Breadcrumb breadcrumb;

    public BorderPage() {
        String className = getClass().getName();

        String shortName = className.substring(className.lastIndexOf('.') + 1);
        String title = ClickUtils.toLabel(shortName);
        addModel("title", title);

        breadcrumb = new Breadcrumb("breadcrumb", 4);
        breadcrumb.setSeperator(" | ");
        breadcrumb.getExcludedPaths().add("login");
        addControl(breadcrumb);
    }

    public String getTemplate() {
        return "/border-template.htm";
    }

    // -------------------------------------------------------- Service Methods

    public CustomerService getCustomerService() {
        return ApplicationRegistry.getInstance().getCustomerService();
    }
}

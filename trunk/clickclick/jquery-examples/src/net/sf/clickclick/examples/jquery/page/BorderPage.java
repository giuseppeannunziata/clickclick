package net.sf.clickclick.examples.jquery.page;

import net.sf.clickclick.examples.jquery.services.ApplicationRegistry;
import net.sf.clickclick.examples.jquery.services.CustomerService;
import net.sf.clickclick.examples.jquery.services.PostCodeService;
import net.sf.clickclick.examples.jquery.util.UIUtils;
import org.apache.click.extras.control.Menu;
import org.apache.click.util.ClickUtils;

public class BorderPage extends BasePage {

    public Menu rootMenu = Menu.getRootMenu();

    public BorderPage() {
        // Set the default JQuery UI style
        UIUtils.style = "ui-lightness";

        String className = getClass().getName();

        String shortName = className.substring(className.lastIndexOf('.') + 1);
        String title = ClickUtils.toLabel(shortName);
        addModel("title", title);

        String srcPath = className.replace('.', '/') + ".java";
        addModel("srcPath", srcPath);
    }

    /**
     * @see #getTemplate()
     */
    public String getTemplate() {
        return "border-template.htm";
    }

    public CustomerService getCustomerService() {
        return ApplicationRegistry.getInstance().getCustomerService();
    }

    public PostCodeService getPostCodeService() {
        return ApplicationRegistry.getInstance().getPostCodeService();
    }
}

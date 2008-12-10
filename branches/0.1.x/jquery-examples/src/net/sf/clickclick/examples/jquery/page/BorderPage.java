package net.sf.clickclick.examples.jquery.page;

import net.sf.click.extras.control.Menu;
import net.sf.click.util.ClickUtils;
import net.sf.clickclick.util.AdvancedPageImports;

public class BorderPage extends BasePage {

    public Menu rootMenu = Menu.getRootMenu();

    public BorderPage() {
        String className = getClass().getName();

        String shortName = className.substring(className.lastIndexOf('.') + 1);
        String title = ClickUtils.toLabel(shortName);
        addModel("title", title);

        String srcPath = className.replace('.', '/') + ".java";
        addModel("srcPath", srcPath);
    }

    public AdvancedPageImports getAdvancedPageImports() {
        return (AdvancedPageImports) getPageImports();
    }

    /**
     * @see #getTemplate()
     */
    public String getTemplate() {
        return "border-template.htm";
    }
}

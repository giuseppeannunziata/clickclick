package net.sf.clickclick.examples.nav;

import net.sf.click.Context;
import net.sf.click.extras.control.Menu;
import net.sf.clickclick.control.menu.FlexiMenu;

public class MenuBuilder {

    private static class MenuHolder {
        private static Menu INSTANCE = createMenus();
    }

    public static Menu getMenus() {
        return MenuHolder.INSTANCE;
    }

    private static Menu createMenus() {
        FlexiMenu rootMenu = new FlexiMenu("rootMenu");

        FlexiMenu menu = createMenu("Home", "home.htm");
        rootMenu.add(menu);

        menu = createMenu("Controls");
        rootMenu.add(menu);

        FlexiMenu subMenu = createMenu("Basics", "controls/html-basics.htm");
        menu.add(subMenu);

        subMenu = createMenu("Menu", "controls/menu.htm");
        menu.add(subMenu);

        menu = createMenu("Layout");
        rootMenu.getChildren().add(menu);

        subMenu = createMenu("Vertical Panel", "layout/vertical-panel-demo.htm");
        menu.getChildren().add(subMenu);

        subMenu = createMenu("Horizontal Panel", "layout/horizontal-panel-demo.htm");
        menu.getChildren().add(subMenu);

        subMenu = createMenu("Grid", "layout/grid-layout-demo.htm");
        menu.getChildren().add(subMenu);

        menu = createMenu("Reload", "reload/resource-bundle.htm");
        rootMenu.add(menu);

        Context context = Context.getThreadLocalContext();
        Class pageClass = context.getPageClass(context.getResourcePath());

        if (pageClass != null) {
            String pageClassName = pageClass.getName();
            String srcPath = pageClassName.replace('.', '/') + ".java";

            menu = createMenu("Page Java", "source-viewer.htm?filename=WEB-INF/classes/" + srcPath);
            menu.setAttribute("target", "_blank");
            rootMenu.add(menu);

            menu = createMenu("Page HTML", "source-viewer.htm?filename=" + context.getPagePath(pageClass));
            menu.setAttribute("target", "_blank");
            rootMenu.add(menu);
        }

        return rootMenu;
    }

    private static FlexiMenu createMenu(String label) {
        FlexiMenu menu = new FlexiMenu();
        menu.setLabel(label);
        menu.setTitle(label);
        return menu;
    }

    private static FlexiMenu createMenu(String label, String path) {
        FlexiMenu menu = new FlexiMenu();
        menu.setLabel(label);
        menu.setPath(path);
        menu.setTitle(label);
        return menu;
    }

    private static FlexiMenu createNamelessMenu(String label, String path, String title) {
        FlexiMenu menu = new FlexiMenu();
        menu.setLabel(label);
        menu.setTitle(title);
        menu.setPath(path);
        return menu;
    }

    private static FlexiMenu createMenu(String name, String label, String path, String title) {
        FlexiMenu menu = new FlexiMenu(name);
        menu.setLabel(label);
        menu.setTitle(title);
        menu.setPath(path);
        return menu;
    }
}
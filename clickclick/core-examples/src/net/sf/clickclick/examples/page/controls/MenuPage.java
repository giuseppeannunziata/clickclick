package net.sf.clickclick.examples.page.controls;

import net.sf.clickclick.control.menu.FlexiMenu;
import net.sf.clickclick.examples.page.BorderPage;

public class MenuPage extends BorderPage {

    FlexiMenu horizontalMenu = new FlexiMenu("horizontalMenu");

    FlexiMenu verticalMenu = new FlexiMenu("verticalMenu");

    public void onInit() {
        horizontalMenu.setOrientation(FlexiMenu.HORIZONTAL);
        populateMenu(horizontalMenu);
        addControl(horizontalMenu);

        verticalMenu.setOrientation(FlexiMenu.VERTICAL);
        populateMenu(verticalMenu);
        addControl(verticalMenu);
    }

    public void populateMenu(FlexiMenu menu) {
        FlexiMenu subMenu = createMenu("Client", "#");
        menu.add(subMenu);
        FlexiMenu addressMenu = createMenu("Address", "#");
        subMenu.add(addressMenu);
        addressMenu.add(createMenu("Physical", "#"));
        addressMenu.add(createMenu("Postal", "#"));
        subMenu = createMenu("Products", "#");
        menu.add(subMenu);
    }

    public FlexiMenu createMenu(String label, String path) {
        FlexiMenu menu = new FlexiMenu();
        menu.setLabel(label);
        menu.setPath(path);
        return menu;
    }
}

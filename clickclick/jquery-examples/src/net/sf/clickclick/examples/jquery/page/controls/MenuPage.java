package net.sf.clickclick.examples.jquery.page.controls;

import net.sf.clickclick.control.menu.FlexiMenu;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.JQMenu;

public class MenuPage extends BorderPage {

    public String title = "Superfish Demo";
    
    JQMenu horizontalMenu = new JQMenu("horizontalMenu");

    JQMenu verticalMenu = new JQMenu("verticalMenu");

    public void onInit() {
       
        horizontalMenu.setOrientation(JQMenu.HORIZONTAL);
        populateMenu(horizontalMenu);
        addControl(horizontalMenu);

        verticalMenu.setOrientation(FlexiMenu.VERTICAL);
        populateMenu(verticalMenu);
        addControl(verticalMenu);
    }
    
    public void populateMenu(JQMenu menu) {
        JQMenu subMenu = createMenu("Client", "#");
        menu.add(subMenu);
        JQMenu addressMenu = createMenu("Address", "#");
        subMenu.add(addressMenu);
        addressMenu.add(createMenu("Physical", "#"));
        addressMenu.add(createMenu("Postal", "#"));
        subMenu = createMenu("Products", "#");
        menu.add(subMenu);
    }

    public JQMenu createMenu(String label, String path) {
        JQMenu menu = new JQMenu();
        menu.setLabel(label);
        menu.setPath(path);
        return menu;
    }
}

package net.sf.clickclick.examples.page.control;

import net.sf.clickclick.examples.page.*;
import net.sf.clickclick.control.Image;

public class ImagePage extends BorderPage {

    public ImagePage() {
        addControl(new Image("image", "/assets/images/penguin.png"));
    }
}

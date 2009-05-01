package net.sf.clickclick.examples.page.repeat;

import org.apache.click.Control;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.Container;
import org.apache.click.control.Field;
import org.apache.click.util.ContainerUtils;
import net.sf.clickclick.control.repeater.Repeater;
import net.sf.clickclick.examples.page.BorderPage;

public class AbstractRepeatPage extends BorderPage {

    protected Repeater repeater;

    // ------------------------------------------------------ Protected Methods

    protected void toggleLinks(int modelSize) {
        if (modelSize == 0) {
            return;
        }

        if (modelSize == 1) {
            Container row = (Container) repeater.getControls().get(0);
            Control control = ContainerUtils.findControlByName(row, "down");
            toggleControl(control, true);
            control = ContainerUtils.findControlByName(row, "up");
            toggleControl(control, true);
        } else if (modelSize == 2) {
            toggleLinksTopAndBottom();
        } else if (modelSize > 2) {
            toggleLinksTopAndBottom();
            toggleLinksSecondFromTopAndBottom();
        }
    }

    // -------------------------------------------------------- Private Methods

    private void toggleControl(Control control, boolean value) {
        if (control instanceof Field) {
            ((Field) control).setDisabled(value);
        } else if (control instanceof AbstractLink) {
            ((AbstractLink) control).setDisabled(value);
        }
    }

    private void toggleLinksTopAndBottom() {
        Container row = (Container) repeater.getControls().get(0);
        Control control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, false);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, true);

        row = (Container) repeater.getControls().get(repeater.getControls().size() - 1);
        control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, true);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, false);
    }

    private void toggleLinksSecondFromTopAndBottom() {
        Container row = (Container) repeater.getControls().get(1);
        Control control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, false);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, false);

        row = (Container) repeater.getControls().get(repeater.getControls().size() - 2);
        control = ContainerUtils.findControlByName(row, "down");
        toggleControl(control, false);
        control = ContainerUtils.findControlByName(row, "up");
        toggleControl(control, false);
    }
}

package net.sf.clickclick.examples.jquery.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.clickclick.control.html.HtmlLabel;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.util.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;

public class TextDemo extends BorderPage {

    private JQActionLink link = new JQActionLink("update", "Request server time", "updateId");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private HtmlLabel label = new HtmlLabel("label", "Server time : " + dateFormat.format(new Date()));

    public TextDemo() {
        addControl(link);
        addControl(label);

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                // Using a CSS selector to replace the Label content with the latest
                // Date
                label.setLabel("Current time : " + dateFormat.format(new Date()));
                partial.replace(label);
                return partial;
            }
        });
    }
}

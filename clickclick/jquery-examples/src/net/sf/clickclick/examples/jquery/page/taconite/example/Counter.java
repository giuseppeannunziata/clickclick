package net.sf.clickclick.examples.jquery.page.taconite.example;

import org.apache.click.Control;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQActionLink;
import net.sf.clickclick.jquery.Command;
import net.sf.clickclick.jquery.Taconite;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Bob Schellink
 */
public class Counter extends BorderPage {

    public JQActionLink link = new JQActionLink("link", "Counter: 0");

    public void onInit() {
        super.onInit();
        link.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(link.getParameter("count"));
                ++count;
                link.setParameter("count", Integer.toString(count));
                link.setLabel("Counter: " + Integer.toString(count));
                Command command = new Command("replace", link).characterData(true);

                // link normally contian '&' which breaks XML parsing. TODO update
                // Click Link's to use &amp; instead

                partial.add(command);
                return partial;
            }
        });
    }
}

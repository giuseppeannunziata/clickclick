package net.sf.clickclick.examples.jquery.page.taconite.example;

import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import net.sf.clickclick.jquery.util.Command;
import net.sf.clickclick.jquery.util.Taconite;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Bob Schellink
 */
public class DecoratorTest extends BorderPage {

    public ActionLink button = new ActionLink("button", "Counter: 0");
    //public ActionLink button2 = new ActionLink("button2", "Counter: 0");

    public void onInit() {
        super.onInit();
        
        button.setAttribute("class", "test");
        //button2.setAttribute("class", "test");
        new JQHelper(button, "a.test").ajaxify();
        
        button.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                int count = NumberUtils.toInt(button.getParameter("count"));
                ++count;
                button.setParameter("count", Integer.toString(count));
                button.setLabel("Counter: " + Integer.toString(count));
                Command command = new Command(Taconite.REPLACE, "a.test",button).characterData(true);

                // link normally contian '&' which breaks XML parsing. TODO update
                // Click Link's to use &amp; instead

                //command.add(button2);
                partial.add(command);
                return partial;
            }
        });
    }
}

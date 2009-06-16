package net.sf.clickclick.examples.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.click.util.AjaxAdapter;
import net.sf.click.Control;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.Partial;
import net.sf.clickclick.control.ajax.AjaxActionLink;
import net.sf.clickclick.examples.page.BorderPage;

public class AjaxDemo extends BorderPage {

    public void onInit() {
        super.onInit();
        // As explained in RawAjaxDemo there are a number of steps to be taken
        // in order to Ajaxify a Click Control. However these steps can easily
        // be hidden by custom controls making Ajax integration much smoother.
        AjaxActionLink link = new AjaxActionLink("link", "Click here make Ajax request");

        // Here we set the unique HTML id attribute so Click can check if an
        // Ajax request targets this link or not
        link.setId("link_id");

        // Create the url to call -> /examples/ajax/ajax-demo.htm
        String url = link.getHref();

        // Set the onclick attribute to invoke the ajax() function with the specified
        // url, parameters and callback function. The callback function in this example is
        // updateLog
        link.setAttribute("onclick", "ajax('" + url + "','',updateLog); return false;");

        // Set an AjaxListener 
        link.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                // Partial represents a partial response from the server.
                // Partial can return any String or byte array, including:
                // JSON, xml and html.
                HtmlStringBuffer buffer = new HtmlStringBuffer();
                buffer.append("<div style='background-color: #EEF1F7; border: 1px solid #6C90CC'>");
                buffer.append("<h2>Ajax Response</h2>");
                buffer.append("<p>This snippet was request by Ajax. The current time is: ");
                buffer.append(getDate());
                buffer.append("</p>");
                buffer.append("</div>");
                return new Partial(buffer.toString());
            }
        });

        addControl(link);
    }

    /**
     * Utility function which formats the current time
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("H:m:s:S");
        return format.format(new Date());
    }
}
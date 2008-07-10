package net.sf.clickclick.examples.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.click.AjaxAdapter;
import net.sf.click.Control;
import net.sf.click.control.ActionLink;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.Partial;
import net.sf.clickclick.examples.page.BorderPage;

public class AjaxDemo extends BorderPage {

    public void onInit() {
        super.onInit();
        ActionLink link = new ActionLink("link", "Click here make Ajax request");

        // Set the id html attribute
        link.setId("link-id");

        // Create the request parameters to send to server -> link-id=1&actionLink=link.
        // There are two key/value pairs send to the server.
        // #1. The link-id=1 key/value pair informs Click which Control is the target of the Ajax call, in this case the ActionLink.
        //     The value of the id does not really matter. As long as the id parameter is present Click will
        //     know the target Control.
        // #2. The actionLink=link key/value pair is the normal parameters sent when clicking on an ActionLink.
        //     The actionLink parameter informs Click of the name of the Link the was clicked and will fire the Control Listener.
        HtmlStringBuffer params = new HtmlStringBuffer();
        params.append(link.getId()).append("=1&");
        params.append(link.ACTION_LINK).append("=").append(link.getName());

        // Create the url to call -> /click-examples/ajax/partial-support-demo.htm
        String url = getContext().getRequest().getContextPath() + getContext().getResourcePath();

        // Set the onclick attribute to invoke the ajax() function with the specified
        // url, parameters and callback function
        link.setAttribute("onclick", "ajax('" + url + "','" + params + "',updateLog); return false;");

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

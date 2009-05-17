package net.sf.clickclick.examples.jquery.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.click.Control;
import net.sf.clickclick.util.AjaxAdapter;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQActionButton;
import net.sf.clickclick.jquery.controls.ajax.JQActionLink;
import net.sf.clickclick.jquery.util.Taconite;
import org.apache.commons.lang.math.RandomUtils;

/**
 *
 * @author Bob Schellink
 */
public class ActionDemo extends BorderPage {

    public void onInit() {
        // Example 1
        // The target div will have its content replaced through Ajax
        final Div target1 = new Div("target1");
        addControl(target1);

        // Create a Ajaxified link that will update a specified target with a
        // Partial response
        JQActionButton button = new JQActionButton("button", "Click here to make Ajax request");

        button.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                // Create a response that will be placed inside the target div
                Taconite response = new Taconite();
                response.replaceContent(target1, createResponse());
                return response;
                //return new Partial(createResponse());
            }
        });
        addControl(button);



        // Example 2
        // The target div will have its content replaced through Ajax
        final Div target2 = new Div("target2");
        addControl(target2);

        // Another target div will have its content replaced through Ajax
        final Div target3 = new Div("target3");
        addControl(target3);

        JQActionLink link = new JQActionLink("link", "Click here to make Ajax request");

        // Provide an alternative message when an Ajax call is made
        link.getJQueryHelper().setIndicatorMessage("\"<h1><img src=\""
            + getContext().getRequest().getContextPath()
            + "/assets/images/indicator.gif\" /> Just a moment...</h1>\"");

        link.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                // This partial will randomly update one target and clear the other

                Taconite response = new Taconite();
                // Randomly update a different target
                if (RandomUtils.nextBoolean()) {
                    response.empty(target3);
                    response.replaceContent(target2, createResponse());
                } else {
                    //activeTargetId = target3.getId();
                    //inactiveTargetId = target2.getId();
                    response.empty(target2);
                    response.replaceContent(target3, createResponse());
                }

                return response;

                // Return normal Javascript that will be executed by JQuery
                //return new Partial("jQuery('#" + activeTargetId + "').html(\"" + createResponse() + "\");" +
                //   "jQuery('#" + inactiveTargetId + "').html(\"\");");
            }
        });
        addControl(link);
    }

    private String createResponse() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append(
            "<div style='background-color: #EEF1F7; border: 1px solid #6C90CC'>");
        buffer.append("<h2>Ajax Response</h2>");
        buffer.append(
            "<p>This snippet was requested via Ajax. The current time is: ");
        buffer.append(getDate());
        buffer.append("</p>");
        buffer.append("</div>");
        return buffer.toString();
    }

    /**
     * Returns a formatted date String.
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:S");
        return format.format(new Date());
    }
}

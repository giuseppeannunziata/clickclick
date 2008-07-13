package net.sf.clickclick.examples.jquery.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.click.util.AjaxAdapter;
import net.sf.click.AjaxControlRegistry;
import net.sf.click.Control;
import net.sf.click.control.AbstractContainer;
import net.sf.click.control.ActionLink;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.JavascriptImport;
import net.sf.clickclick.control.JavascriptInclude;
import net.sf.click.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.click.util.AdvancedPageImports;

/**
 * A JQuery based Ajax Demo containing a link <a> and div <div>. When the link
 * is clicked it retrieves the time from the server via Ajax, and update the 
 * content of the div.
 */
public class AjaxDemo extends BorderPage {

    public String title = "Ajax Demo";
    
    public void onInit() {
        super.onInit();

        JQAjaxLink ajaxLink = new JQAjaxLink("link", "Click here make Ajax request");

        // Register an AjaxAdapter with the JQAjaxLink, which returns the latest
        // time from the server
        ajaxLink.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                HtmlStringBuffer buffer = new HtmlStringBuffer();
                buffer.append("<div style='background-color: #EEF1F7; border: 1px solid #6C90CC'>");
                buffer.append("<h2>Ajax Response</h2>");
                buffer.append("<p>This snippet was requested via Ajax. The current time is: ");
                buffer.append(getDate());
                buffer.append("</p>");
                buffer.append("</div>");
                return new Partial(buffer.toString());
            }
        });

        // Create a log which content will updated through an Ajax response.
        Log log = new Log("log", "log");
        addControl(log);

        // By setting the ajaxLink targetId to the Div's id, the div contents 
        // will be replaced by the Partial text.
        ajaxLink.setTargetId(log.getId());

        addControl(ajaxLink);
    }

    /**
     * Returns a formatted date String.
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:S");
        return format.format(new Date());
    }

    /**
     * An Log component that is the target of an Ajax response.
     */
    class Log extends AbstractContainer {

        public Log(String name, String id) {
            super(name);
            setId(id);
        }

        public String getTag() {
            return "div";
        }
    }

    /**
     * Demonstrates a custom JQuery based AJAX link, which generates the
     * necessary javascript to callback a registered AjaxListener on the JQAjaxLink.
     * 
     * The JQAjaxLink also enables markup replacement the HTML element with the
     * specified targetId.
     */
    public static class JQAjaxLink extends ActionLink {

        private String targetId;
        
        private JavascriptInclude javascript = new JavascriptInclude();

        public JQAjaxLink() {
        }

        public JQAjaxLink(String name, String label) {
            super(name, label);
            setId(name + "_id");
        }

        public void onInit() {
            AjaxControlRegistry.registerAjaxControl(this);
        }

        /**
         * Generates the JQuery
         */
        public String getHtmlImports() {
            AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();
            String url = getContext().getRequest().getContextPath() + getContext().
                getResourcePath();

            // Set JQuery to noConflict mode, so it play nice with other Javascript
            // libraries
            String jquery = "jQuery.noConflict();\n"
                // Build the callback function
                + "jQuery(document).ready(function(){\n"
                + "  jQuery('#" + getId() + "').click(\n"
                + "    function(){\n"
                + "        jQuery.get('" + url + "',{" + getId() + ":'1'," + ACTION_LINK + ":'" + getName() + "'}\n";

                if (getTargetId() != null) {
                    jquery += "          ,function(data) {\n"
                    // Add animation effect by first hiding the log
                    + "            jQuery('#" + getTargetId() + "').hide();\n"
                    + "            jQuery('#" + getTargetId() + "').html(data);\n"

                    // and then making the log visible
                    + "            jQuery('#" + getTargetId() + "').show('fast');\n"
                    + "          }\n";
            }
            jquery += "        ); return false;\n"
            + "  })})\n";

            pageImports.add(new JavascriptInclude(jquery));

            // Include reference to the JQuery library
            pageImports.add(new JavascriptImport(getContext().getRequest().
                getContextPath() + "/clickclick/jquery/jquery-1.2.6.js"));

            return null;
        }

        public JavascriptInclude getJavascriptInclude() {
            return javascript;
        }
        
        public void setJavascriptInclude(JavascriptInclude javascript) {
            this.javascript = javascript;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }
    }
}


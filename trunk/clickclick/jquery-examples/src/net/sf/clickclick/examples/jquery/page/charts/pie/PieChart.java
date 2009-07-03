package net.sf.clickclick.examples.jquery.page.charts.pie;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 */
public class PieChart extends BorderPage {

    public void onGet() {
        // Render PieChart Page JavaScript
        Map model = new HashMap();
        model.put("data", getPieData());
        model.put("label", "Browser usage % for " + Calendar.getInstance().get(Calendar.YEAR));
        getHeadElements().add(new JsScript("/charts/pie/pie-chart.js", model));
    }

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import excanvas.js for Internet Explorer which doesn't support the canvas element
            JsImport jsImport = new JsImport("/clickclick/example/jqplot/excanvas.min.js");
            jsImport.setConditionalComment(JsImport.IF_IE);
            headElements.add(jsImport);

            // Import jquery.js
            headElements.add(new JsImport(JQHelper.jqueryImport));

            // Import JQPlot libraries
            headElements.add(new JsImport("/clickclick/example/jqplot/jquery.jqplot.min.js"));
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.pieRenderer.min.js"));
            headElements.add(new CssImport("/clickclick/example/jqplot/jquery.jqplot.min.css"));
        }
        return headElements;
    }

    private String getPieData() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("['Internet Explorer',40],");
        buffer.append("['Firefox',45],");
        buffer.append("['Safari',8],");
        buffer.append("['Opera',7]");
        return buffer.toString();
    }
}

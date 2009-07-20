package net.sf.clickclick.examples.jquery.page.charts.line;

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
public class InteractiveChart extends BorderPage {

    public void onGet() {
        // Render Chart Page JavaScript
        Map model = new HashMap();
        model.put("data", getChartData());
        model.put("label", "Interactive demo");
        getHeadElements().add(new JsScript("/charts/line/interactive-chart.js", model));
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
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"));
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.canvasTextRenderer.min.js"));
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.dateAxisRenderer.min.js"));
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.dragable.min.js"));
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.highlighter.min.js"));
                headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.trendline.min.js"));
            headElements.add(new CssImport("/clickclick/example/jqplot/jquery.jqplot.min.css"));

        }
        return headElements;
    }

    private String getChartData() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(20);
        buffer.append("['2009-01-1', 400000],");
        buffer.append("['2009-02-1', 420000],");
        buffer.append("['2009-03-1', 410000],");
        buffer.append("['2009-03-31', 430000]");
        return buffer.toString();
    }
}
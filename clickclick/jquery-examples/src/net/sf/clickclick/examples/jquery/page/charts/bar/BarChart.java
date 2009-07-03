package net.sf.clickclick.examples.jquery.page.charts.bar;

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
public class BarChart extends BorderPage {

    public void onGet() {
        // Render BarChart Page JavaScript
        Map model = new HashMap();
        model.put("data", getChartData());
        model.put("label", "Browser usage %");
        getHeadElements().add(new JsScript("/charts/bar/bar-chart.js", model));
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
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.barRenderer.min.js"));
            headElements.add(new JsImport("/clickclick/example/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"));
            headElements.add(new CssImport("/clickclick/example/jqplot/jquery.jqplot.min.css"));
        }
        return headElements;
    }

    private String getChartData() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(20);
        buffer.append("[[2006,69],[2007,55], [2008,40], [2009,35]],");
        buffer.append("[[2006,25],[2007,35], [2008,45], [2009,50]],");
        buffer.append("[[2006,5] ,[2007,6] , [2008,8] , [2009,8]],");
        buffer.append("[[2006,1] ,[2007,4] , [2008,7] , [2009,7]]");
        return buffer.toString();
    }
}

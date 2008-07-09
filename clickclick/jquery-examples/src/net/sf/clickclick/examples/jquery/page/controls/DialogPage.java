package net.sf.clickclick.examples.jquery.page.controls;

import java.util.HashMap;
import java.util.Map;
import net.sf.click.control.Button;
import net.sf.click.control.CssInclude;
import net.sf.click.control.JavascriptInclude;
import net.sf.click.util.PageImports;

import net.sf.clickclick.jquery.dialog.JQDialog;
import net.sf.clickclick.examples.jquery.page.BorderPage;

public class DialogPage extends BorderPage {

    public String title = "UI Dialog Demo";

    public void onInit() {
        JQDialog dialog = new JQDialog("dialog");
        dialog.setStyle("display", "none");
        Button button = new Button("button");
        button.setAttribute("onclick", "jQuery('#dialog').dialog('close');");
        dialog.add(button);
        addControl(dialog);
    }

    public void onHtmlImports(PageImports pageImports) {
        Map model = new HashMap();
        String javascript = getContext().renderTemplate("controls/dialog.js", model);
        JavascriptInclude jsInclude = new JavascriptInclude(javascript);
        pageImports.add(jsInclude);

        String css = getContext().renderTemplate("controls/dialog.css", model);
        CssInclude cssInclude = new CssInclude(css);
        pageImports.add(cssInclude);
    }
}

package net.sf.clickclick.examples.jquery.page.controls;

import java.util.HashMap;
import java.util.Map;
import net.sf.click.control.Button;
import net.sf.click.control.JavascriptInclude;
import net.sf.click.util.PageImports;

import net.sf.clickclick.jquery.dialog.JQDialog;
import net.sf.clickclick.examples.jquery.page.BorderPage;

public class DialogPage extends BorderPage {

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
        JavascriptInclude jsInclude = new JavascriptInclude();
        String javascript = getContext().renderTemplate("/net/sf/clickclick/examples/jquery/page/controls/DialogPageImports.js", model);
        jsInclude.append(javascript);
        pageImports.add(jsInclude);
        super.onHtmlImports(pageImports);
    }
}

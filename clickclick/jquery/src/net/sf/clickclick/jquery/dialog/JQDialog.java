package net.sf.clickclick.jquery.dialog;

import net.sf.click.control.CssImport;
import net.sf.click.control.JavascriptImport;
import net.sf.click.util.PageImports;
import net.sf.clickclick.control.html.Div;

/**
 *
 * @author Bob Schellink
 */
public class JQDialog extends Div {

    public JQDialog() {
        this(null);
    }

    public JQDialog(String name) {
        super(name);
        setAttribute("class", "flora");
    }

    public void onHtmlImports(PageImports pageImports) {
        String contextPath = getContext().getRequest().getContextPath();
        CssImport cssImport = new CssImport(contextPath + "/clickclick/jquery/flora.dialog.css");
        cssImport.setAttribute("media", "screen");
        pageImports.add(cssImport);
        cssImport = new CssImport(contextPath + "/clickclick/jquery/flora.resizable.css");
        cssImport.setAttribute("media", "screen");
        pageImports.add(cssImport);

        JavascriptImport jsImport = new JavascriptImport(contextPath
            + "/clickclick/jquery/jquery-1.2.6.js");
        pageImports.add(jsImport);

        jsImport = new JavascriptImport(contextPath + "/clickclick/jquery/jquery.ui.all.js");
        pageImports.add(jsImport);
    }
}

package net.sf.clickclick.jquery.controls;

import net.sf.clickclick.control.html.Div;
import org.apache.click.util.PageImports;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.element.CssImport;

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

    public String getHtmlImports() {
        PageImports pageImports = getPage().getPageImports();
        String contextPath = getContext().getRequest().getContextPath();
        CssImport cssImport = new CssImport(contextPath
            + "/clickclick/jquery/ui/flora.dialog.css");
        cssImport.setAttribute("media", "screen");
        pageImports.add(cssImport);

        cssImport = new CssImport(contextPath
            + "/clickclick/jquery/ui/flora.resizable.css");
        cssImport.setAttribute("media", "screen");
        pageImports.add(cssImport);

        JsImport jsImport = new JsImport(contextPath
            + "/clickclick/jquery/jquery-1.2.6.js");
        pageImports.add(jsImport);

        jsImport = new JsImport(contextPath
            + "/clickclick/jquery/ui/ui.core.js");
        pageImports.add(jsImport);
        
        jsImport = new JsImport(contextPath
            + "/clickclick/jquery/ui/ui.draggable.js");
        pageImports.add(jsImport);
        
        jsImport = new JsImport(contextPath
            + "/clickclick/jquery/ui/ui.resizable.js");
        pageImports.add(jsImport);
        
        jsImport = new JsImport(contextPath
            + "/clickclick/jquery/ui/ui.dialog.js");
        pageImports.add(jsImport);
        return null;
    }
}

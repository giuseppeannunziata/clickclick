package net.sf.clickclick.mootools.controls;

import net.sf.clickclick.control.ajax.AjaxForm;
import org.apache.click.util.PageImports;
import org.apache.click.element.JsImport;

/**
 *
 * @author Bob Schellink
 */
public class MTAjaxForm extends AjaxForm {

    public MTAjaxForm() {
        
    }

    public MTAjaxForm(String name) {
        super(name);
    }

    public String getHtmlImports() {
        PageImports pageImports = getPage().getPageImports();

        String contextPath = getContext().getRequest().getContextPath();
        pageImports.add(new JsImport(contextPath + "/clickclick/mootools/mootools-1.2.js"));
        
        return null;
    }
}

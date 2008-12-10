package net.sf.clickclick.mootools.controls;

import net.sf.clickclick.control.ajax.AjaxForm;
import net.sf.clickclick.util.AdvancedPageImports;
import net.sf.clickclick.util.JavascriptImport;

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
        AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();

        String contextPath = getContext().getRequest().getContextPath();
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/mootools/mootools-1.2.js"));
        
        return null;
    }
}

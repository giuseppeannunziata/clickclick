package net.sf.clickclick.mootools.controls;

import net.sf.click.AjaxControlRegistry;
import net.sf.click.control.Form;
import net.sf.click.control.HiddenField;
import net.sf.click.control.JavascriptImport;
import net.sf.click.util.PageImports;

/**
 *
 * @author Bob Schellink
 */
public class MTAjaxForm extends Form {

    public String title = "MooTools Ajax Demo";
    
    public MTAjaxForm(String name) {
        super(name);
    }

    public void onInit() {
        super.onInit();
        add(new HiddenField(name, getId()));
        AjaxControlRegistry.registerAjaxControl(this);
    }
    
    public void onHtmlImports(PageImports pageImports) {
        String contextPath = getContext().getRequest().getContextPath();
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/mootools/mootools-1.2.js"));
    }
}

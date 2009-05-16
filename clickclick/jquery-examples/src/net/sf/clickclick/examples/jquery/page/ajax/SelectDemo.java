package net.sf.clickclick.examples.jquery.page.ajax;

import java.util.HashMap;
import java.util.Map;
import net.sf.clickclick.util.AjaxAdapter;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.element.CssStyle;
import org.apache.click.element.JsScript;
import org.apache.click.control.Form;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.click.util.PageImports;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.JQSelect;

public class SelectDemo extends BorderPage {

    public String title = "Select Demo";

    private Select provinceSelect = new JQSelect("provinceSelect", "Select a Province:");

    private Select citySelect = new Select("citySelect", "Select a City:");

    public Form form = new Form("form");

    public void onInit() {
        super.onInit();

        provinceSelect.setActionListener(new AjaxAdapter() {
            public Partial onAjaxAction(Control source) {
                String provinceCode = getContext().getRequestParameter(provinceSelect.getName());
                return populateCities(provinceCode);
            }
        });

        //When page is initialized, load provinces.
        populateProvinces();
        form.add(provinceSelect);
        form.add(citySelect);
    }

    public String getHtmlImports() {
        PageImports pageImports = getPageImports();

        Context context = getContext();
        Map model = new HashMap();
        String contextPath = context.getRequest().getContextPath();
        model.put("context", contextPath);

        String cssTemplate = "/ajax/select-demo.css";
        pageImports.add(new CssStyle(context.renderTemplate(cssTemplate, model)));

        String jsTemplate = "/ajax/select-demo.js";
        pageImports.add(new JsScript(context.renderTemplate(jsTemplate, model)));
        
        return null;
    }
    
    // -------------------------------------------------------- Private Methods

    private void populateProvinces() {
        provinceSelect.add(Option.EMPTY_OPTION);
        provinceSelect.add(new Option("GAU", "Gauteng"));
        provinceSelect.add(new Option("WC", "Western Cape"));
        provinceSelect.add(new Option("N", "KwaZulu Natal"));
    }

    private Partial populateCities(String provinceCode) {
        if (provinceCode == null) {
            return null;
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer();

        if ("GAU".equals(provinceCode)) {
            new Option("PTA", "Pretoria").render(citySelect, buffer);
            new Option("JHB", "Johannesburg").render(citySelect, buffer);
            new Option("CEN", "Centurion").render(citySelect, buffer);
        } else if ("WC".equals(provinceCode)) {
            new Option("CT", "Cape Town").render(citySelect, buffer);
            new Option("G", "George").render(citySelect, buffer);
        } else if ("N".equals(provinceCode)) {
            new Option("DBN", "Durban").render(citySelect, buffer);
        }

        return new Partial(buffer.toString());
    }
}

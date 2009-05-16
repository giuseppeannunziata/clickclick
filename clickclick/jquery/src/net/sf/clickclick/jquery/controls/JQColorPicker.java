package net.sf.clickclick.jquery.controls;

import java.util.HashMap;
import java.util.Map;
import org.apache.click.Context;
import org.apache.click.control.TextField;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.html.Div;
import org.apache.click.util.PageImports;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.element.CssImport;
import org.apache.click.element.CssStyle;


/**
 *
 * @author Bob Schellink
 */
public class JQColorPicker extends TextField {
    
    private Div image = new Div();
    
    public JQColorPicker() {
    }
    
    public JQColorPicker(String name) {
        super(name);
    }
    
    public void onRender() {
        image.setId(getId() + "_image");
        image.setAttribute("class", "colorPickerImage");
    }

    public String getHtmlImports() {
        PageImports pageImports = getPage().getPageImports();
        Context context = getContext();
        String contextPath = context.getRequest().getContextPath();

        String resource = contextPath + "/clickclick/jquery/colorpicker/css/colorpicker.css";
        pageImports.add(new CssImport(resource));

        resource = contextPath + "/clickclick/jquery/jquery-1.2.6.js";
        pageImports.add(new JsImport(resource));
        
        resource = contextPath + "/clickclick/jquery/colorpicker/js/colorpicker.js";
        pageImports.add(new JsImport(resource));
        
        Map model = new HashMap();
        model.put("fieldId", getId());
        model.put("imageId", image.getId());
        model.put("context", contextPath);

        String include = getContext().renderTemplate("/clickclick/jquery/colorpicker/jq-color-picker.js", model);
        JsScript jsInclude = new JsScript(include);
        pageImports.add(jsInclude);

        String style = getContext().renderTemplate("/clickclick/jquery/colorpicker/jq-color-picker.css", model);
        CssImport cssInclude = new CssImport(style);
        pageImports.add(cssInclude);
        return null;
    }

    public void render(HtmlStringBuffer buffer) {
        super.render(buffer);
        Div colorPickerBorder = new Div();
        colorPickerBorder.setAttribute("class", "colorPickerSelector");
        image.add(colorPickerBorder);

        Div colorPickerBackground = new Div();
        colorPickerBorder.add(colorPickerBackground);
        colorPickerBackground.setAttribute("style", "background-color: #"
            + getValue());

        image.render(buffer);
    }
}

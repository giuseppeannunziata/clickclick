package net.sf.clickclick.jquery.colorpicker;

import net.sf.click.Context;
import net.sf.click.control.CssImport;
import net.sf.click.control.CssInclude;
import net.sf.click.control.JavascriptImport;
import net.sf.click.control.JavascriptInclude;
import net.sf.click.control.TextField;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.PageImports;
import net.sf.clickclick.control.html.Div;

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

    public void onHtmlImports(PageImports pageImports) {
        Context context = getContext();
        String contextPath = context.getRequest().getContextPath();

        String resource = contextPath + "/clickclick/jquery/colorpicker/css/colorpicker.css";
        pageImports.add(new CssImport(resource));

        resource = contextPath + "/clickclick/jquery/jquery-1.2.6.js";
        pageImports.add(new JavascriptImport(resource));
        
        resource = contextPath + "/clickclick/jquery/colorpicker/js/colorpicker.js";
        pageImports.add(new JavascriptImport(resource));
        
        String fieldId = getId();
        String imageId = image.getId();
        String include =  "$(document).ready(function(){\n"
                + "$('#" + imageId + "').click(function() {\n"
                + "  $('#" + fieldId + "').click();\n"
                + "})\n"
                + "$('#" + fieldId + "').ColorPicker({\n"
                + "  onSubmit: function(hsb, hex, rgb) {\n"
                + "    $('#" + fieldId + "').val(hex);\n"
                + "    $('#" + imageId + " div div').css('backgroundColor', '#' + hex);\n"
                + "  },\n"
                + "  onBeforeShow: function () {\n"
                + "    $(this).ColorPickerSetColor(this.value);\n"
                + " }\n"
                + "})\n"
                + ".bind('keyup', function(){\n"
                + "  $(this).ColorPickerSetColor(this.value);\n"
                + "});\n"
                + "})\n";

        JavascriptInclude jsInclude = new JavascriptInclude(include);
        pageImports.add(jsInclude);
        
        String style = ".colorPickerSelector {\n"
                + "  position: absolute;\n"
                + "  top: 0;\n"
                + "  left: 0;\n"
                + "  width: 21px;\n"
                + "  height: 21px;\n"
                + "  background: url(" + contextPath + "/clickclick/jquery/colorpicker/images/select21.png);\n"
                + "}\n"
                + ".colorPickerSelector div {\n"
                + "  position: absolute;\n"
                + "  top: 3px;\n"
                + "  left: 3px;\n"
                + "  width: 15px;\n"
                + "  height: 15px;\n"
                + "  background: url(" + contextPath + "/clickclick/jquery/colorpicker/images/select21.png) center;\n"
                + "}\n"
                + ".colorPickerImage {\n"
                + "  display: inline;\n"
                + "  position: relative;\n"
                + "  height: 21px;\n"
                + "  width: 21px;\n"
                + "}";
        
        CssInclude cssInclude = new CssInclude(style);
        pageImports.add(cssInclude);
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

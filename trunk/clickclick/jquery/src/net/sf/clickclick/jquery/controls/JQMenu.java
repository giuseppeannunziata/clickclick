package net.sf.clickclick.jquery.controls;

import java.util.Iterator;
import net.sf.click.Context;
import net.sf.click.extras.control.Menu;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.CssImport;
import net.sf.clickclick.control.JavascriptImport;
import net.sf.clickclick.control.JavascriptInclude;
import net.sf.clickclick.control.menu.FlexiMenu;
import net.sf.click.util.AdvancedPageImports;

/**
 * Based on the JQuery plugin, Superfish -> http://users.tpg.com.au/j_birch/plugins/superfish/
 *
 * @author Bob Schellink
 */
public class JQMenu extends FlexiMenu {

    private String options = "animation : { opacity:'show', height:'show' }, speed: 'fast'";
    
    public JQMenu() {        
    }
    
    public JQMenu(String name) {
        super(name);
    }
    
    public String getOptions() {
        return options;
    }

    /**
     * Please see the following link on how to set the menu Options:
     *
     * http://users.tpg.com.au/j_birch/plugins/superfish/#options
     * 
     * @param options
     */
    public void setOptions(String options) {
        this.options = options;
    }

    public String getHtmlImports() {
        AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();
        Context context = getContext();
        String contextPath = context.getRequest().getContextPath();

        String resource = contextPath +
            "/clickclick/jquery/superfish/css/superfish.css";
        pageImports.add(new CssImport(resource));

        if (VERTICAL.equals(getOrientation())) {
            resource = contextPath +
                "/clickclick/jquery/superfish/css/superfish-vertical.css";
            pageImports.add(new CssImport(resource));
        }

        resource = contextPath + "/clickclick/jquery/jquery-1.2.6.js";
        pageImports.add(new JavascriptImport(resource));

        resource = contextPath +
            "/clickclick/jquery/superfish/js/hoverIntent.js";
        pageImports.add(new JavascriptImport(resource));

        resource = contextPath +
            "/clickclick/jquery/superfish/js/bgiframe.js";
        pageImports.add(new JavascriptImport(resource));

        resource = contextPath +
            "/clickclick/jquery/superfish/js/superfish.js";
        pageImports.add(new JavascriptImport(resource));

        String include = "$(document).ready(function(){" 
            + "$('ul.sf-menu')"
            + ".superfish({" + getOptions() + "})" 
            + ".find('>li:has(ul)')"
            + ".mouseover(function(){" 
            + "  $('ul', this).bgIframe({opacity:false});" 
            + "})"
            + ".find('a')"
            + ".focus(function(){" 
            + "  $('ul', $('.nav>li:has(ul)')).bgIframe({opacity:false});" 
            + "});"
            + "});";
        JavascriptInclude jsInclude = new JavascriptInclude(include);
        jsInclude.setUnique(true);
        pageImports.add(jsInclude);
        return null;
    }

    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("ul");
        String cssClass = "sf-menu";
        if (VERTICAL.equals(getOrientation())) {
            cssClass += " sf-vertical";
        }
        buffer.appendAttribute("class", cssClass);
        appendAttributes(buffer);
        buffer.closeTag();
        buffer.append("\n");
        renderMenu(buffer, this);
        buffer.elementEnd("ul");
    }

    protected void renderMenu(HtmlStringBuffer buffer, Menu menu) {
        Iterator it = menu.getChildren().iterator();
        while (it.hasNext()) {
            Menu child = (Menu) it.next();
            if (child.isUserInRoles()) {
                buffer.elementStart("li");
                if (child.getChildren().size() == 0) {
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuItem(buffer, child);
                } else {
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenuItem(buffer, child);
                    buffer.elementStart("ul");
                    buffer.closeTag();
                    buffer.append("\n");
                    renderMenu(buffer, child);
                    buffer.elementEnd("ul");
                    buffer.append("\n");
                }
                buffer.elementEnd("li");
                buffer.append("\n");
            }
        }
    }
}

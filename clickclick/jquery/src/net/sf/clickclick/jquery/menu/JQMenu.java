package net.sf.clickclick.jquery.menu;

import java.util.Iterator;
import net.sf.click.Context;
import net.sf.click.control.CssImport;
import net.sf.click.control.JavascriptImport;
import net.sf.click.control.JavascriptInclude;
import net.sf.click.extras.control.Menu;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.PageImports;
import net.sf.clickclick.control.menu.FlexiMenu;

/**
 *
 * @author Bob Schellink
 */
public class JQMenu extends FlexiMenu {

    public JQMenu() {        
    }
    
    public JQMenu(String name) {
        super(name);
    }
    
    public String getHtmlImports() {
        return null;
    }

    public void onHtmlImports(PageImports pageImports) {
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
            + ".superfish({"
            + "  animation : { opacity:'show', height:'show' },"
            + "  speed: 'fast'" 
            + "})" 
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

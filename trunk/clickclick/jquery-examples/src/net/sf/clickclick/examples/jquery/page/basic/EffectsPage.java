package net.sf.clickclick.examples.jquery.page.basic;

import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

public class EffectsPage extends BorderPage {

    public String title = "Basic";

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import jquery.js, basic.js and basic.css
            headElements.add(new JsImport(JQHelper.jqueryImport));
            headElements.add(new JsImport("/basic/effects.js"));
            headElements.add(new CssImport("/basic/effects.css"));
        }
        return headElements;
    }
}

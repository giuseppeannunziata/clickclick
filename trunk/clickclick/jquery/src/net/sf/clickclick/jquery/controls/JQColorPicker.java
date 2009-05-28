/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.jquery.controls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.click.control.TextField;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;

/**
 * Provides a ColorPicker control based on the JQuery ColorPicker plugin:
 * http://www.eyecon.ro/colorpicker/.
 *
 * @author Bob Schellink
 */
public class JQColorPicker extends TextField {

    // -------------------------------------------------------------- Constants

    /**
     * The ColorPicker JS library:
     * "<tt>/clickclick/jquery/colorpicker/js/colorpicker.js</tt>".
     */
    public static String colorpickerJsImport =
        "/clickclick/jquery/colorpicker/js/colorpicker.js";

    /**
     * The ColorPicker default CSS:
     * "<tt>/clickclick/jquery/colorpicker/css/colorpicker.css</tt>".
     */
    public static String colorpickerCssImport =
        "/clickclick/jquery/colorpicker/css/colorpicker.css";

    /**
     * The ColorPicker CSS Template:
     * "<tt>/clickclick/jquery/colorpicker/jquery.colorpicker.style.css</tt>".
     */
    public static String colorPickerStyleImport =
        "/clickclick/jquery/colorpicker/jquery.colorpicker.style.css";

    // -------------------------------------------------------------- Variables

    /** The color picker image div. */
    protected Div image = new Div();

    /**
     * The ColorPicker JavaScript template:
     * "<tt>/clickclick/jquery/template/colorpicker/jquery.colorpicker.template.js</tt>".
     */
    protected String template = "/clickclick/jquery/template/colorpicker/jquery.colorpicker.template.js";

    /** The color picker JavaScript template model. */
    protected Map jsModel;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default JQColorPicker.
     */
    public JQColorPicker() {
    }

    /**
     * Create a JQColorPicker with the given name.
     *
     * @param name the name of the control
     */
    public JQColorPicker(String name) {
        if (name != null) {
            setName(name);
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Set the name of the color picker.
     *
     * @param name the name of the control
     */
    public void setName(String name) {
        super.setName(name);
        image.setId(getId() + "_image");
    }

    /**
     * Return the color picker JavaScript template.
     *
     * @return the template the JavaScript template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the color picker JavaScript template.
     *
     * @param template the JavaScript template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Return the JavaScript template model.
     *
     * @return the JavaScript template model
     */
    public Map getJsModel() {
        if(jsModel == null) {
            jsModel = new HashMap();
        }
        return jsModel;
    }

    /**
     * Set the JavaScript template model.
     *
     * @param model the JavaScript template model
     */
    public void setJsModel(Map model) {
        this.jsModel = model;
    }

    /**
     * Return the JQColorPicker resources: {@link #colorpickerCssImport},
     * {@link net.sf.clickclick.jquery.helper.JQHelper#JQUERY_IMPORTS},
     * {@link #colorpickerJsImport},
     * {@link #COLORPICKER_CSS_TEMPLATE} and
     * {@link #COLORPICKER_JS_TEMPLATE}.
     *
     * @return the list of head elements
     */
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            headElements.add(new CssImport(colorpickerCssImport));
            headElements.add(new JsImport(JQHelper.jqueryImport));
            headElements.add(new JsImport(colorpickerJsImport));
        }

        addJsTemplate(headElements);
        addCustomCssImport(headElements);
        return headElements;
    }

    /**
     * Render the HTML representation of the JQColorPicker.
     *
     * @param buffer the buffer to render to
     */
    public void render(HtmlStringBuffer buffer) {
        super.render(buffer);

        image.setAttribute("class", "colorPickerImage");

        Div colorPickerBorder = new Div();
        colorPickerBorder.setAttribute("class", "colorPickerSelector");
        image.add(colorPickerBorder);

        Div colorPickerBackground = new Div();
        colorPickerBorder.add(colorPickerBackground);
        colorPickerBackground.setAttribute("style", "background-color: #"
            + getValue());

        image.render(buffer);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Add the JQColorPicker JavaScript {@link #template} to the list of head elements.
     * <p/>
     * You can override this method to add your own template.
     *
     * @param headElements the list of head elements to include for this control
     */
    protected void addJsTemplate(List headElements) {
        String id = getId();
        if (id == null) {
            throw new IllegalStateException("Color picker name is not set.");
        }

        JsScript jsScript = new JsScript();
        jsScript.setId(id + "_jqcolorpicker_js");

        if (!headElements.contains(jsScript)) {
            // Create the data model to pass to the templates
            Map model = getJsModel();
            model.put("fieldId", id);
            model.put("imageId", image.getId());

            jsScript.setModel(model);
            jsScript.setTemplate(getTemplate());
            headElements.add(jsScript);
        }
    }

    /**
     * Add the JQColorPicker CSS style import to the list of head elements.
     * <p/>
     * The CSS style import is: <tt>{@link #colorPickerStyleImport</tt>.
     * <p/>
     * You can override this method to add your own custom import.
     *
     * @param headElements the list of head elements to include for this control
     */
    protected void addCustomCssImport(List headElements) {
        headElements.add(new CssImport(colorPickerStyleImport));
    }
}

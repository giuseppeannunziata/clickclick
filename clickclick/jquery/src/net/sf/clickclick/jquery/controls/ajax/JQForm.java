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
package net.sf.clickclick.jquery.controls.ajax;

import java.util.List;
import net.sf.clickclick.control.ajax.AjaxForm;
import net.sf.clickclick.jquery.helper.JQFormHelper;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide an Ajax enabled Form control.
 * <p/>
 * <b>Please note:</b> JQForm uses {@link net.sf.clickclick.jquery.helper.JQHelper}
 * for Ajax functionality.
 *
 * @author Bob Schellink
 */
public class JQForm extends AjaxForm {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    private JQHelper jqHelper = new JQFormHelper(this);

    /** The JavaScript focus function HEAD element. */
    private JsScript focusScript;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default JQForm.
     */
    public JQForm() {
    }

    /**
     * Create a JQForm with the given name.
     *
     * @param name the name of the form
     */
    public JQForm(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the JQuery Helper instance.
     *
     * @return the jqHelper instance
     */
    public JQHelper getJQueryHelper() {
        return jqHelper;
    }

    /**
     * Set the JQuery Helper instance.
     *
     * @param jqHelper the JQuery Helper instance
     */
    public void setJQueryHelper(JQHelper jqHelper) {
        this.jqHelper = jqHelper;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize the JQForm Ajax functionality.
     */
    public void onInit() {
        super.onInit();
        jqHelper.ajaxify();
    }

    /**
     * Set the JavaScript client side form validation flag.
     *
     * @param validate the JavaScript client side validation flag
     */
    public void setJavaScriptValidation(boolean validate) {
        super.setJavaScriptValidation(validate);
        jqHelper.getModel().put("javascriptValidate", validate);
    }

    /**
     * Return JQForm HEAD elements.
     *
     * @return the JQForm HEAD elements
     */
    public List getHeadElements() {
        if (!getContext().isAjaxRequest()) {
            return super.getHeadElements();
        } else {
            if (headElements == null) {
                headElements = super.getHeadElements();

                focusScript = new JsScript();
                headElements.add(focusScript);
            }
            return headElements;
        }
    }

    /**
     * Render the Form field focus JavaScript to the string buffer.
     *
     * @param buffer the StringBuffer to render to
     * @param formFields the list of form fields
     */
    protected void renderFocusJavaScript(HtmlStringBuffer buffer,
        List formFields) {
        if (!getContext().isAjaxRequest()) {
            super.renderFocusJavaScript(buffer, formFields);
        } else {
            HtmlStringBuffer tempBuf = new HtmlStringBuffer();
            super.renderFocusJavaScript(tempBuf, formFields);
            String temp = tempBuf.toString();
            if (StringUtils.isBlank(temp)) {
                return;
            }
            String prefix = "<script type=\"text/javascript\"><!--";
            temp = temp.substring(prefix.length());
            String suffix = "//--></script>\n";
            int end = temp.indexOf(suffix);
            temp = temp.substring(0, end);
            focusScript.setContent(temp);
        }
    }

    /**
     * Render the given form start tag and the form hidden fields to the given
     * buffer.
     *
     * @param buffer the HTML string buffer to render to
     * @param formFields the list of form fields
     */
    protected void renderHeader(HtmlStringBuffer buffer, List formFields) {
        if (getJavaScriptValidation()) {
            // The default implementation renders an inline onsubmit handler on form.
            // Here we skip rendering that inilne onsubmit handler which is instead
            // handled by the jquery.form.template.js
            setJavaScriptValidation(false);
            super.renderHeader(buffer, formFields);
            setJavaScriptValidation(true);
        } else {
            super.renderHeader(buffer, formFields);
        }
    }
}

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

import net.sf.clickclick.control.ajax.AjaxForm;
import net.sf.clickclick.jquery.helper.JQFormHelper;
import net.sf.clickclick.jquery.helper.JQHelper;

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
}

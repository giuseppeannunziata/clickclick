/*
 * Copyright 2008 Bob Schellink
 *
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
package net.sf.clickclick.control.ajax;

import net.sf.click.AjaxControlRegistry;
import net.sf.click.AjaxListener;
import net.sf.click.control.Button;

/**
 *
 * @author Bob Schellink
 */
public class AjaxButton extends Button {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a Submit button with the given name.
     *
     * @param name the button name
     */
    public AjaxButton(String name) {
        super(name);
    }

    /**
     * Create a Submit button with the given name and label.
     *
     * @param name the button name
     * @param label the button display label
     */
    public AjaxButton(String name, String label) {
        super(name, label);
    }

    /**
     * Create a Submit button with the given name, listener object and
     * listener method.
     *
     * @param name the button name
     * @param ajaxListener the Ajax Listener target object
     */
    public AjaxButton(String name, AjaxListener ajaxListener) {
        super(name);
        setActionListener(ajaxListener);
    }

    /**
     * Create a Submit button with the given name, label, listener object and
     * listener method.
     *
     * @param name the button name
     * @param label the button display label
     * @param ajaxListener the Ajax Listener target object
     * @throws IllegalArgumentException if listener is null or if the method
     * is blank
     */
    public AjaxButton(String name, String label, AjaxListener ajaxListener) {
        super(name, label);
        setActionListener(ajaxListener);
    }

    /**
     * Create an Submit button with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public AjaxButton() {
        super();
    }

    // ------------------------------------------------------ Public Attributes

    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);
    }
}

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
package net.sf.click;

import net.sf.click.AjaxListener;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.click.util.Partial;
import org.apache.commons.lang.Validate;

/**
 * Extends ControlRegistry to provide a thread local register for managing Ajax
 * controls and ActionListener events.
 * <p/>
 * Developers who implement their own controls, should look at the example in
 * the {@link ControlRegistry} JavaDoc.
 * <p/>
 * Registering Ajax Controls for processing is done as follows:
 *
 * <pre class="prettyprint">
 * public void onInit() {
 *     Form form = new Form("form");
 *
 *     // Ajaxify the form by registering it in the ControlRegistry
 *     ControlRegistry.registerAjaxControl(form);
 *
 *     Submit submit = new Submit("submit");
 *     submit.setListener(new AjaxListener() {
 *
 *         public Partial onAjaxAction(Control control) {
 *             return new Partial("Hello World!");
 *         }
 *     });
 * } </pre>
 *
 * @author Bob Schellink
 * @author Malcolm Edgar
 */
public class AjaxControlRegistry extends ControlRegistry {

    // -------------------------------------------------------- Variables

    /** The set of unique registered Ajax Controls. */
    private Set ajaxControlList;

    // --------------------------------------------------------- Public Methods

    /**
     * Register the control to be processed by the ClickServlet for Ajax
     * requests.
     *
     * @param control the control to register
     */
    public static void registerAjaxControl(Control control) {
        Validate.notNull(control, "Null control parameter");

        AjaxControlRegistry instance = (AjaxControlRegistry) getThreadLocalRegistry();
        Set controlList = instance.getAjaxControls();
        controlList.add(control);
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * Checks if any Ajax controls have been registered.
     */
    boolean hasAjaxControls() {
        if (ajaxControlList == null || ajaxControlList.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Process all the registered controls and return true if the page should
     * continue processing.
     *
     * @return true if the page should continue processing or false otherwise
     */
    boolean processAjaxControls(Context context) {

        if (!hasAjaxControls()) {
            return true;
        }

        for (Iterator it = ajaxControlList.iterator(); it.hasNext();) {
            Control control = (Control) it.next();

            // Check if control is targeted by this request
            if (context.getRequestParameter(control.getId()) != null) {
                control.onProcess();
            }
        }

        // Fire the registered listeners
        return fireActionEvents(context);
    }

    /**
     * Fire all the registered action events and return true if the page should
     * continue processing.
     *
     * @return true if the page should continue processing or false otherwise
     */
    boolean fireActionEvents(Context context) {
        boolean continueProcessing = true;

        if (!hasActionEvents()) {
            return true;
        }

        for (int i = 0, size = eventSourceList.size(); i < size; i++) {
            Control source = (Control) eventSourceList.get(i);
            ActionListener listener = (ActionListener) eventListenerList.get(i);

            if (context.isAjaxRequest() && listener instanceof AjaxListener) {

                Partial partial = ((AjaxListener) listener).onAjaxAction(source);
                if (partial != null) {
                    // Have to process Partial here
                    partial.process(context);
                }

                // Ajax requests stops further processing
                continueProcessing = false;

            } else {
                if (!listener.onAction(source)) {
                    continueProcessing = false;
                }
            }
        }

        return continueProcessing;
    }

    /**
     * Clear the registry.
     */
    void clearRegistry() {
        if (hasAjaxControls()) {
            ajaxControlList.clear();
        }
        super.clearRegistry();
    }

    // -------------------------------------------------------- Private Methods

    /**
     * Return the set of unique Ajax Controls.
     *
     * @return set of unique Ajax Controls
     */
    private Set getAjaxControls() {
        if (ajaxControlList == null) {
            ajaxControlList = new LinkedHashSet();
        }
        return ajaxControlList;
    }

}

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
package org.apache.click;

import net.sf.click.*;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.click.control.ActionButton;
import org.apache.click.control.ActionLink;
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

    // -------------------------------------------------------------- Constants

    /**
     * Indicates the listener should fire in the Ajax phase which is processed
     * <tt>BEFORE</tt> the onProcess phase.
     * Listeners in this phase are <tt>guaranteed</tt> to trigger, even when
     * redirecting, forwarding or processing stopped.
     */
    public static final int ON_AJAX_EVENT = 300;

    // -------------------------------------------------------- Variables

    /** The set of unique registered Ajax Controls. */
    private Set ajaxControlList;

    /** The AJAX events holder. */
    private EventHolder ajaxEventHolder;

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

    /**
     * Checks if any Ajax controls have been registered.
     */
    public boolean hasAjaxControls() {
        if (ajaxControlList == null || ajaxControlList.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Process all ajax controls and return true if the page should
     * continue processing.
     *
     * @return true if the page should continue processing or false otherwise
     */
    public boolean processAjaxControls(Context context) {

        if (!hasAjaxControls()) {
            return true;
        }

        for (Iterator it = ajaxControlList.iterator(); it.hasNext();) {
            Control control = (Control) it.next();

            // Check if control is targeted by this request
            String id = control.getId();
            if (id != null && context.getRequestParameter(id) != null) {
                control.onProcess();
            } else {
                // Handle edge cases for ActionLink and ActionButton where ID
                // might not be defined
                String name = control.getName();
                if (name != null) {
                    boolean clicked = name.equals(context.getRequestParameter(
                        ActionLink.ACTION_LINK));
                    if (!clicked) {
                        clicked = name.equals(context.getRequestParameter(
                            ActionButton.ACTION_BUTTON));
                    }
                    if (clicked) {
                        control.onProcess();
                    }
                }
            }
        }

        // Fire the registered listeners
        return fireActionEvents(context, POST_ON_PROCESS_EVENT);
    }

    // ------------------------------------------------ Package Private Methods

    boolean fireActionEvents(Context context, List eventSourceList,
        List eventListenerList) {
        boolean continueProcessing = true;

        for (int i = 0, size = eventSourceList.size(); i < size; i++) {
            Control source = (Control) eventSourceList.get(i);
            ActionListener listener =
                (ActionListener) eventListenerList.get(i);

            if (context.isAjaxRequest() && listener instanceof AjaxListener) {

                Partial partial = ((AjaxListener) listener).onAjaxAction(
                    source);

                // Ensure we execute the POST_ON_RENDER_EVENT for Ajax events
                fireActionEvents(context, AjaxControlRegistry.POST_ON_RENDER_EVENT);

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

    EventHolder getEventHolder(int phase) {
        if (phase == ON_AJAX_EVENT) {
            return getAjaxEventHolder();
        } else {
           return super.getEventHolder(phase);
        }
    }

    EventHolder getAjaxEventHolder() {
        if (ajaxEventHolder == null) {
            ajaxEventHolder = new EventHolder();
        }
        return ajaxEventHolder;
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

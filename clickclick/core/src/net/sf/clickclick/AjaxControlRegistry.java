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
package net.sf.clickclick;

import org.apache.click.*;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.click.control.ActionButton;
import org.apache.click.control.ActionLink;
import net.sf.clickclick.util.Partial;
import org.apache.commons.lang.Validate;

/**
 * Extends ControlRegistry to provide a thread local register for managing Ajax
 * controls and ActionListener events.
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
    public static final int ON_AJAX_EVENT = 250;

    // -------------------------------------------------------- Variables

    /** The set of unique registered Ajax Controls. */
    private Set ajaxControlList;

    /** The AJAX events holder. */
    private EventHolder ajaxEventHolder;

    /** Track the last event that was fired. */
    private int lastEventFired = -1;

    /**
     * Guard against firing the postOnRenderEvent more than once for Ajax
     * requests.
     */
    private boolean postOnRenderEventFired = false;

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
     * Return the thread local registry instance.
     *
     * @return the thread local registry instance.
     * @throws RuntimeException if a ControlRegistry is not available on the
     * thread.
     */
    protected static AjaxControlRegistry getThreadLocalRegistry() {
        return (AjaxControlRegistry) ControlRegistry.getThreadLocalRegistry();
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

    /**
     * For Ajax requests this method will fire the Ajax listener and if a
     * {@link net.sf.clickclick.util.Partial} is returned, stream it back to
     * the browser.
     *
     * @see org.apache.click.ControlRegistry#fireActionEvent(org.apache.click.Context, org.apache.click.Control, org.apache.click.ActionListener, int)
     *
     * @param context the request context
     * @param source the source control
     * @param listener the listener to fire
     * @param event the specific event which events to fire
     *
     * @return true if the page should continue processing or false otherwise
     */
    protected boolean fireActionEvent(Context context, Control source,
        ActionListener listener, int event) {

        this.lastEventFired = event;

        boolean continueProcessing = true;

        if (context.isAjaxRequest() && listener instanceof AjaxListener) {

            Partial partial = ((AjaxListener) listener).onAjaxAction(source);

            // Guard against firing the POST_ON_RENDER_EVENT more than once
            // for Ajax requests
            if (!postOnRenderEventFired) {
                postOnRenderEventFired = true;

                // Ensure we execute the POST_ON_RENDER_EVENT for Ajax events
                fireActionEvents(context, AjaxControlRegistry.POST_ON_RENDER_EVENT);
            }

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

        return continueProcessing;
    }

    /**
     * @see org.apache.click.ControlRegistry#fireActionEvents(org.apache.click.Context, int)
     *
     * @param context the request context
     * @param event the event which listeners to fire
     *
     * @return true if the page should continue processing or false otherwise
     */
    protected boolean fireActionEvents(Context context, int event) {
        return super.fireActionEvents(context, event);
    }

    /**
     * @see org.apache.click.ControlRegistry#getEventHolder(int)
     *
     * @param event the event which EventHolder to retrieve
     *
     * @return the EventHolder for the specified event
     */
    protected EventHolder getEventHolder(int phase) {
        if (phase == ON_AJAX_EVENT) {
            return getAjaxEventHolder();
        } else {
           return super.getEventHolder(phase);
        }
    }

    /**
     * Allow the Registry to handle the error that occurred.
     */
    protected void errorOccurred(Throwable throwable) {
        if (hasAjaxControls()) {
            ajaxControlList.clear();
        }
        lastEventFired = -1;
        getEventHolder(ON_AJAX_EVENT).clear();
        super.errorOccurred(throwable);
    }

    /**
     * Clear the registry.
     */
    protected void clearRegistry() {
        if (hasAjaxControls()) {
            ajaxControlList.clear();
        }
        lastEventFired = -1;
        postOnRenderEventFired = false;
        super.clearRegistry();
    }

    /**
     * Create a new EventHolder instance.
     *
     * @param event the EventHolder's event
     * @return new EventHolder instance
     */
    protected EventHolder createEventHolder(int event) {
        return new AjaxEventHolder(event);
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * Return the EventHolder for the {@link #ON_AJAX_EVENT}.
     *
     * @return the Ajax EventHolder
     */
    EventHolder getAjaxEventHolder() {
        if (ajaxEventHolder == null) {
            ajaxEventHolder = createEventHolder(ON_AJAX_EVENT);
        }
        return ajaxEventHolder;
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

    // ---------------------------------------------------------- Inner Classes

    /**
     * Extends EventHolder to provide special Ajax handling.
     */
    public class AjaxEventHolder extends EventHolder {

        /**
         * Construct a new AjaxEventHolder for the given event.
         *
         * @param event the AjaxEventHolder's event 
         */
        public AjaxEventHolder(int event) {
            super(event);
        }

        /**
         * Register the event source and event ActionListener to be fired in the
         * specified event.
         *
         * @param source the action event source
         * @param listener the event action listener
         * @param event the specific event to trigger the action event
         */
        public void registerActionEvent(Control source, ActionListener listener) {
            super.registerActionEvent(source, listener);

            if (event == ON_AJAX_EVENT && event < lastEventFired) {
                // If the Ajax event for which this listener is registering
                // already fired, trigger the listener immediately.
                // This feature is useful for stateful pages where controls are
                // added in the listener or onRender method
                AjaxControlRegistry.this.fireActionEvent(Context.getThreadLocalContext(),
                    source, listener, event);
            }
        }
    }
}

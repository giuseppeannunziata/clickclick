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
package net.sf.clickclick;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.clickclick.util.Partial;
import org.apache.click.*;
import org.apache.click.control.ActionButton;
import org.apache.click.control.ActionLink;
import org.apache.click.util.PageImports;

/**
 * Provides extra functionality not available in ClickServlet.
 * <p/>
 * Some of the features provided here are:
 * <ul>
 *   <li>Full Ajax support</li>
 * </ul>
 *
 * @author Bob Schellink
 */
public class ClickClickServlet extends ClickServlet {

    // ------------------------------------------------------ Protected Methods

    /**
     * Creates and returns a new ActionEventDispatcher instance.
     *
     * @return the new ControlRegistry instance
     */
    protected ActionEventDispatcher createActionEventDispatcher() {
        return new AjaxControlRegistry();
    }

    /**
     * Extends the default Page processing to support Ajax requests.
     *
     * @see org.apache.click.ClickServlet#processPage(org.apache.click.Page)
     *
     * @param page the Page to process
     * @throws Exception if an error occurs
     */
    protected void processPage(Page page) throws Exception {

        final Context context = page.getContext();

        // Non Ajax requests are handled by default implementation
        if (!context.isAjaxRequest()) {
            super.processPage(page);
            return;
        }

        AjaxControlRegistry controlRegistry = AjaxControlRegistry.getThreadLocalRegistry();

        PageImports pageImports = createPageImports(page);
        page.setPageImports(pageImports);

        boolean continueProcessing = performOnSecurityCheck(page, context);

        if (continueProcessing) {
            performOnInit(page, context);

            // Check if this is a legacy Ajax request. Legacy Ajax requests
            // don't have Ajax Controls registered on the ControlRegistry
            if (controlRegistry.hasAjaxControls()) {

                // Process Ajax controls
                performAjaxProcessing(page, context, controlRegistry);

            } else {
                // This is a legacy Ajax request and will have the same
                // event callbacks (life cycle) as a normal non-ajax request.
                continueProcessing = performOnProcess(page, context, controlRegistry);

                if (continueProcessing) {
                    performOnPostOrGet(page, context, context.isPost());

                    performOnRender(page, context);
                }

                controlRegistry.fireActionEvents(context, AjaxControlRegistry.POST_ON_RENDER_EVENT);

                performRender(page, context);
            }
        } else {
            // If security check fails for an Ajax request, Click returns without
            // any rendering. It is up to the user to render a Partial response
            // in the onSecurityCheck event
        }
    }

    /**
     * Perform specialized Ajax processing namely:
     * <ul>
     * <li>delegate to {@link #processAjaxControls(org.apache.click.Context, net.sf.clickclick.AjaxControlRegistry)}
     * to find target Ajax controls and perform their onProcess event callback
     * </li>
     * <li>fire any action listener registered for the
     * {@link AjaxControlRegistry#POST_ON_PROCESS_EVENT onProcess} event
     * callback.</li>
     * <li>fire any action listener registered for the
     * {@link AjaxControlRegistry#POST_ON_RENDER_EVENT onRender} event
     * callback</li>
     * <li>{@link net.sf.clickclick.util.Partial#process(org.apache.click.Context) process}
     * and {@link net.sf.clickclick.util.Partial#render(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) render}
     * a response for all Partials that was accumulated during the Ajax Control's
     * onProcess event.
     * </li>
     * </ul>
     *
     * @param page the page to process
     * @param context the request context
     * @param ajaxControlRegistry the request control registry
     * @return true if processing should continue, false otherwise
     */
    protected boolean performAjaxProcessing(Page page, Context context,
        AjaxControlRegistry ajaxControlRegistry) {

        // Perform ON_AJAX_EVENT after the onInit event which ensures the
        // Control ID has been set, Controls have been added to Containers,
        // Repeater has adjusted its child IDs etc.
        // The ON_AJAX_EVENT allows a control's Javascript to be modified
        // as needed
        ajaxControlRegistry.fireActionEvents(context,
                                             AjaxControlRegistry.ON_AJAX_EVENT);

        // Ajax requests are processed separately
        // TODO: Ajax doesn't support forward, is still necessary to check isForward?
        if (context.isAjaxRequest() && !context.isForward()) {

            if (ajaxControlRegistry.hasAjaxControls()) {

                // Perform onProcess for regsitered Ajax controls
                processAjaxControls(ajaxControlRegistry, context);

                // Fire listeners registered during the onProcess event. This
                // step will accumulate Partials that are returned by registered
                // AjaxListeners
                ajaxControlRegistry.fireActionEvents(context, AjaxControlRegistry.POST_ON_PROCESS_EVENT);

                // Ensure we execute the POST_ON_RENDER_EVENT for Ajax events
                ajaxControlRegistry.fireActionEvents(context, AjaxControlRegistry.POST_ON_RENDER_EVENT);

                List partials = ajaxControlRegistry.getPartials();

                // Render a response consisting of the list of partials
                renderPartials(partials, context);

                // Since Ajax Controls was registered, stop further processing
                return false;
            }
            // If no Ajax Controls was registered, continue processing (for
            // backwards compatibility)
        }
        return true;
    }

    /**
     * Render a response for all the given Partial objects that was accumulated
     * during the Ajax Control's onProcess event.
     * <p/>
     * <b>Please note</b> as a side effect this method will remove the Partial
     * instances from the partials list as they are rendered
     *
     * @param partials list of Partial responses
     * @param context the request context
     */
    protected void renderPartials(List<Partial> partials, Context context) {

        // Process and render a response for all Partials that was
        // accumulated during the Ajax Control's onProcess event. Partials
        // will render directly to the output stream so no further
        // rendering is needed beyond this point
        for (int i = 0, size = partials.size(); i < size; i++) {
            Partial partial = partials.get(0);

            // Pop the first entry in the list
            partials.remove(0);

            // Process and render the Partial response
            partial.process(context);
        }
    }

    /**
     * Process all ajax controls and return true if the page should
     * continue processing.
     *
     * @param ajaxControlRegistry the request control registry
     * @param context the request context
     * @return true if the page should continue processing or false otherwise
     */
    protected boolean processAjaxControls(AjaxControlRegistry ajaxControlRegistry,
        Context context) {

        if (!ajaxControlRegistry.hasAjaxControls()) {
            return true;
        }

        boolean continueProcessing = true;

        for (Iterator it = ajaxControlRegistry.getAjaxControls().iterator(); it.hasNext();) {
            Control control = (Control) it.next();

            // Check if control is targeted by this request
            String id = control.getId();
            if (id != null && context.getRequestParameter(id) != null) {

                // Process the control
                if (!control.onProcess()) {
                    continueProcessing = false;
                }

            } else {
                // Handle edge cases for ActionLink and ActionButton where ID
                // might not be defined
                String name = control.getName();
                if (name != null) {
                    boolean clicked = name.equals(
                        context.getRequestParameter(ActionLink.ACTION_LINK));

                    if (!clicked) {
                        clicked = name.equals(
                            context.getRequestParameter(ActionButton.ACTION_BUTTON));
                    }

                    if (clicked) {

                        // Process the control
                        if (!control.onProcess()) {
                            continueProcessing = false;
                        }
                    }
                }
            }
        }

        return continueProcessing;
    }

    /**
     * Extends the application exception handling to cater for Ajax requests.
     * When an exception occurs during an Ajax request, the exception
     * stack trace is streamed back to the browser.
     *
     * @see org.apache.click.ClickServlet#handleException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean, java.lang.Throwable, java.lang.Class)
     *
     * @param request the servlet request with the associated error
     * @param response the servlet response
     * @param isPost boolean flag denoting the request method is "POST"
     * @param exception the error causing exception
     * @param pageClass the page class with the error
     */
    protected void handleException(HttpServletRequest request,
        HttpServletResponse response, boolean isPost, Throwable exception,
        Class pageClass) {

        if (Context.hasThreadLocalContext()) {
            Context context = Context.getThreadLocalContext();
            if (context.isAjaxRequest()) {
                try {
                    // If an exception occurs during an Ajax request, stream
                    // the exception instead of creating an ErrorPage

                    PrintWriter writer = null;
                    try {
                        writer = response.getWriter();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                        // TODO: use return an ErrorReport instance instead
                        writer.write("<error>\n");
                        exception.printStackTrace(writer);
                        writer.write("\n</error>");
                    } finally {
                        if (writer != null) {
                            writer.flush();
                        }
                    }
                } catch (Throwable error) {
                    logger.error(error.getMessage(), error);
                    throw new RuntimeException(error);
                }
                logger.error("Error occurred while processing Ajax request", exception);
                return;
            }
        }
        super.handleException(request, response, isPost, exception, pageClass);
    }
}

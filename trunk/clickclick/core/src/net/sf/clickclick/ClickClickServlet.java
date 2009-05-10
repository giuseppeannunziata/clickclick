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

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.click.*;
import org.apache.click.util.ErrorPage;
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
     * Creates and returns a new ControlRegistry instance.
     *
     * @return the new ControlRegistry instance
     */
    protected ControlRegistry createControlRegistry() {
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
        final boolean isPost = context.isPost();

        PageImports pageImports = createPageImports(page);
        page.setPageImports(pageImports);

        AjaxControlRegistry controlRegistry = AjaxControlRegistry.getThreadLocalRegistry();

        // Support direct access of click-error.htm
        if (page instanceof ErrorPage) {
            ErrorPage errorPage = (ErrorPage) page;
            errorPage.setMode(configService.getApplicationMode());

            controlRegistry.errorOccurred(errorPage.getError());
        }

        boolean continueProcessing = performOnSecurityCheck(page, context);

        if (continueProcessing) {
            performOnInit(page, context);

            // Perform Ajaxify event after the onInit event which ensures the
            // Control ID has been set, Controls have been added to Containers,
            // Repeater has adjusted its child IDs etc.
            // The Ajaxify event allows a control's Javascript to be modified
            // as needed
            controlRegistry.fireActionEvents(context, AjaxControlRegistry.ON_AJAX_EVENT);

            // Check if Ajax processing is needed
            if (!performAjaxProcessing(page, context, controlRegistry)) {
                return;
            }

            continueProcessing = performOnProcess(page, context, controlRegistry);

            if (continueProcessing) {
                performOnPostOrGet(page, context, isPost);

                performOnRender(page, context);
            }
        }

        controlRegistry.fireActionEvents(context, AjaxControlRegistry.POST_ON_RENDER_EVENT);

        performRender(page, context);
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

    // ------------------------------------------------ Package Private Methods

    /**
     * Perform Ajax processing phase.
     *
     * @param page the page to process
     * @param context the request context
     * @param controlRegistry the request control registry
     * @return true if processing should continue, false otherwise
     */
    boolean performAjaxProcessing(Page page, Context context,
        AjaxControlRegistry ajaxControlRegistry) {

        // Ajax requests are processed separately
        if (context.isAjaxRequest() && !context.isForward()) {
            if (ajaxControlRegistry.hasAjaxControls()) {
                ajaxControlRegistry.processAjaxControls(context);

                // As Ajax Controls was registered, stop further processing
                return false;
            }
            // If no Ajax Controls was reigstered, continue processing (for
            // backwards compatibility)
        }
        return true;
    }
}

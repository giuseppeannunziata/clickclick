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
public class ClickServlet2 extends ClickServlet {

    // ------------------------------------------------------ Protected Methods

    /**
     * Creates and returns a new ControlRegistry instance.
     *
     * @return the new ControlRegistry instance
     */
    protected ControlRegistry createControlRegistry() {
        return new AjaxControlRegistry();
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * @see ClickServlet#onProcessCheck(net.sf.click.Context, net.sf.click.ControlRegistry)
     *
     * @param page the page to process
     * @param context the request context
     * @param controlRegistry the request control registry
     * @return true if processing should continue, false otherwise
     */
    boolean onProcessCheck(Page page, Context context, ControlRegistry controlRegistry) {
        if (controlRegistry instanceof AjaxControlRegistry) {
            AjaxControlRegistry ajaxControlRegistry = (AjaxControlRegistry) controlRegistry;
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
        }
        return true;
    }
}

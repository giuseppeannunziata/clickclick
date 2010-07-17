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
package net.sf.clickclick.util;

import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.Page;
import org.apache.click.Partial;
import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.ClassUtils;

/**
 * Provides an action listener that returns a Partial instance instead of a
 * boolean. If the Partial is not null it will be rendered and further Page
 * processing is stopped. If null is returned Page processing continues normally.
 * <p/>
 * <b>Please note:</b> if the Page forward or redirect property was set during
 * the action listener, page processing is also stopped whether a Partial was
 * returned nor not.
 */
public abstract class PartialActionListener implements ActionListener {

    public final boolean onAction(Control source) {
        Page page = ClickUtils.getParentPage(source);
        if (page == null) {
            String controlClassName = ClassUtils.getShortClassName(source.getClass());
            throw new IllegalStateException("control '" + source.getName() + "' "
                + controlClassName + " parent page is not set");
        }

        Partial partial = onPartialAction(source);
        if (partial != null) {
            partial.render(Context.getThreadLocalContext());
            return false;
        } else {
            // Check if forward or redirect was set in the action
            if (page.getForward() == null && page.getRedirect() == null) {
                return true;
            } else {
                // If redirect or forward was set skip further processing
                return false;
            }
        }
    }

    public abstract Partial onPartialAction(Control source);
}

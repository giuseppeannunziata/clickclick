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
package net.sf.clickclick.jquery.controls;

import java.util.List;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

/**
 * Provide a Dialog control based on the JQuery UI Dialog widget:
 * http://docs.jquery.com/UI/Dialog.
 *
 * @author Bob Schellink
 */
public class JQDialog extends Div {

    // -------------------------------------------------------------- Constants

    /**
     * The JQuery UI Dialog default CSS:
     * "<tt>/clickclick/jquery/ui/flora.dialog.css</tt>".
     */
    public static String jqueryUIDialogCssImport =
        "/clickclick/jquery/ui/flora.dialog.css";

    /**
     * The JQDialog UI Resizable CSS:
     * "<tt>/clickclick/jquery/ui/flora.resizable.css</tt>".
     */
    public static String jqueryUIResizableCssImport =
        "/clickclick/jquery/ui/flora.resizable.css";

    /**
     * The JQuery UI Core JS library:
     * "<tt>/clickclick/jquery/ui/ui.core.js</tt>".
     */
    public static String jqueryUICoreImport =
        "/clickclick/jquery/ui/ui.core.js";

    /**
     * The JQuery UI Draggable JS library:
     * "<tt>/clickclick/jquery/ui/ui.draggable.js</tt>".
     */
    public static String jqueryUIDraggableImport =
        "/clickclick/jquery/ui/ui.draggable.js";

    /**
     * The JQuery UI Resizable JS library:
     * "<tt>/clickclick/jquery/ui/ui.resizable.js</tt>".
     */
    public static String jqueryUIResizableJsImport =
        "/clickclick/jquery/ui/ui.resizable.js";

     /**
     * The JQuery UI Dialog JS library:
     * "<tt>/clickclick/jquery/ui/ui.dialog.js</tt>".
     */
    public static String jqueryUIDialogJsImport =
        "/clickclick/jquery/ui/ui.dialog.js";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default dialog.
     */
    public JQDialog() {
        this(null);
    }

    /**
     * Create a dialog with the given name.
     *
     * @param name the name of the dialog
     */
    public JQDialog(String name) {
        super(name);
        setAttribute("class", "flora");
    }

    /**
     * Return the JQDialog resources: {@link #jqueryUIDialogCssImport},
     * {@link #jqueryUIResizableCssImport},
     * {@link net.sf.clickclick.jquery.helper.JQHelper#jqueryImport},
     * {@link #jqueryUICoreImport},
     * {@link #jqueryUIDraggableImport},
     * {@link #jqueryUIResizableJsImport} and
     * {@link #jqueryUIDialogJsImport}.
     *
     * @return the list of head elements
     */
    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            CssImport cssImport = new CssImport(jqueryUIDialogCssImport);
            cssImport.setAttribute("media", "screen");
            headElements.add(cssImport);

            cssImport = new CssImport(jqueryUIResizableCssImport);
            cssImport.setAttribute("media", "screen");
            headElements.add(cssImport);

            JsImport jsImport = new JsImport(JQHelper.jqueryImport);
            headElements.add(jsImport);

            jsImport = new JsImport(jqueryUICoreImport);
            headElements.add(jsImport);

            jsImport = new JsImport(jqueryUIDraggableImport);
            headElements.add(jsImport);

            jsImport = new JsImport(jqueryUIResizableJsImport);
            headElements.add(jsImport);

            jsImport = new JsImport(jqueryUIDialogJsImport);
            headElements.add(jsImport);
        }
        return headElements;
    }
}

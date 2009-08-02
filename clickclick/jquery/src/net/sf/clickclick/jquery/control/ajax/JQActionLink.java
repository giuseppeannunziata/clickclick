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
package net.sf.clickclick.jquery.control.ajax;

import net.sf.clickclick.AjaxListener;
import net.sf.clickclick.control.ajax.AjaxActionLink;
import net.sf.clickclick.jquery.helper.JQHelper;

/**
 * Provide an Ajax enabled ActionLink control.
 * <p/>
 * <b>Please note:</b> JQActionLink uses {@link net.sf.clickclick.jquery.helper.JQHelper}
 * to provide the Ajax functionality.
 * <p/>
 * Below is an example showing how to refresh a Table when the link is clicked:
 *
 * <pre class="prettyprint">
 * private JQActionLink link = new JQActionLink("link");
 *
 * public MyPage() {
 *     addControl(link);
 *
 *     // Set an Ajax listener on the link that return a Taconite (Partial)
 *     // instance
 *     link.setActionListener(new AjaxAdapter() {
 *
 *         public Partial onAjaxAction(Control source) {
 *             Taconite partial = new Taconite();
 *
 *             // Create a table instance
 *             Table table = createTable();
 *
 *             // Refresh the table by replacing it with the latest data on the server
 *             partial.replace(table);
 *
 *             return partial;
 *         }
 *     });
 *
 * } </pre>
 */
public class JQActionLink extends AjaxActionLink {

    // -------------------------------------------------------------- Variables

    /** The JQuery helper object. */
    private JQHelper jqHelper = new JQHelper(this);

    // ----------------------------------------------------------- Constructors

    /**
     * Create a JQActionLink with the given name.
     *
     * @param name the name of the control
     */
    public JQActionLink(String name) {
        super(name);
    }

    /**
     * Create a JQActionLink with the given name and label.
     *
     * @param name the name of the control
     * @param label the label of the control
     */
    public JQActionLink(String name, String label) {
        super(name, label);
    }

    /**
     * Create a JQActionLink with the given name, label and id.
     *
     * @param name the name of the control
     * @param label the label of the control
     * @param id the id of the control
     */
    public JQActionLink(String name, String label, String id) {
        super(name, label, id);
    }

    /**
     * Create a JQActionLink with the given listener.
     *
     * @param ajaxListener the listener of the control
     */
    public JQActionLink(AjaxListener ajaxListener) {
        super(ajaxListener);
    }

    /**
     * Create a JQActionLink with the given name and listener.
     *
     * @param name the name of the control
     * @param ajaxListener the listener of the control
     */
    public JQActionLink(String name, AjaxListener ajaxListener) {
        super(name, ajaxListener);
    }

    /**
     * Create a JQActionLink with the given name, label and listener.
     *
     * @param name the name of the control
     * @param label the label of the control
     * @param ajaxListener the listener of the control
     */
    public JQActionLink(String name, String label, AjaxListener ajaxListener) {
        super(name, label, ajaxListener);
    }

    /**
     * Create a default JQActionLink.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public JQActionLink() {
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
     * Initialize the JQActionLink Ajax functionality.
     */
    public void onInit() {
        super.onInit();
        jqHelper.ajaxify();
    }
}

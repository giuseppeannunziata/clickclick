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
package net.sf.clickclick.jquery.helper;

import java.util.Map;
import org.apache.click.Control;

/**
 * Provide a specialized JQuery helper that triggers Ajax request at specified
 * intervals.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.refresh.template.js.txt">here</a>
 * to view the template.
 * <p/>
 * JQRefreshHelper can either be embedded inside Click controls or used to decorate
 * the control.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom control with an embedded JQRefreshHelper that
 * enables Ajax behavior:
 *
 * <pre class="prettyprint">
 * public class Heartbeat extends Div {
 *
 *     // The embedded JQuery helper object.
 *     private JQHelper jqHelper = new JQRefreshHelper(this);
 *
 *     // Constructor
 *     public Heartbeat(String name, int interval) {
 *         super(name);
 *         jqHelper.setInterval(interval);
 *
 *         // The div name will be used as the refreshId
 *         jqHelper.setRefreshId(name);
 *     }
 *
 *     // Initialize the Ajax functionality
 *     public void onInit() {
 *         super.onInit();
           jqHelper.ajaxify();
 *     }
 * } </pre>
 *
 * <h3>Decorate example</h3>
 *
 * Below is an example how to decorate a Div control (clock) to updated its timer
 * at specific intervals:
 *
 * <pre class="prettyprint">
 * public class RefreshDemo extends BorderPage {
 *
 *     private Div clock = new Div("clock");
 *     private Text value = new Text();
 *     private JQActionLink stop = new JQActionLink("stop", "Stop refresh");
 *
 *     // RefreshID is used by the JQRefreshHelper to interact with JavaScript
 *     // functions added by the helper JavaScript template.
 *     private String refreshId = "refresh";
 *
 *     private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
 *
 *     public RefreshDemo() {
 *         addControl(clock);
 *         addControl(stop);
 *
 *         value.setText(getTime());
 *         clock.add(value);
 *
 *         clock.setActionListener(new AjaxAdapter() {
 *
 *              public Partial onAjaxAction(Control source) {
 *                  Taconite partial = new Taconite();
 *
 *                  // Replace the clock with the updated clock
 *                  partial.replaceContent(clock, value);
 *                  return partial;
 *              }
 *         });
 *
 *         stop.setActionListener(new AjaxAdapter(){
 *            public Partial onAjaxAction(Control source) {
 *                Taconite partial = new Taconite();
 *
 *                 // Note the eval statement below. This executes raw JavaScript
 *                 // on the browser, in this case a function from this helper's
 *                 // template: Click.refresh.stop('refresh');
 *                 partial.eval("Click.refresh.stop('" + refreshId + "');");
 *
 *                return partial;
 *            }
 *        });
 *
 *         JQRefreshHelper helper = new JQRefreshHelper(clock, refreshId, 2000);
 *         helper.ajaxify();
 *     }
 *
 *     private String getTime() {
 *         return format.format(new Date());
 *     }
 * } </pre>
 *
 * <h3>JavaScript</h3>
 * The JavaScript template exposes three public methods that can be interacted
 * with from your Java code. They are:
 * <ul>
 * <li>Click.refresh.start(refreshId, interval); - start the Ajax requests</li>
 * <li>Click.refresh.stop(refreshId); - stop the Ajax requests</li>
 * <li>Click.refresh.update(refreshId, interval); - updates to the new interval</li>
 * </ul>
 *
 * In order to invoke these methods use the {@link net.sf.clickclick.jquery.util.Taconite#eval(java.lang.String)}
 * command to execute raw JavaScript in the browser. For example:
 *
 * <pre class="prettyprint">
 * String refreshId = "refresh";
 *
 * JQActionLink start = new JQActionLink("start", "Start refresh (5 second intervals)");
 *
 * start.setActionListener(new AjaxAdapter(){
 *     public Partial onAjaxAction(Control source) {
 *         Taconite partial = new Taconite();
 *
 *         // Execute raw JavaScript using eval
 *         partial.eval("Click.refresh.start('" + refreshId + "', 5000);");
 *          return partial;
 *     }
 * });
 *
 * JQActionLink update = new JQActionLink("update", "Update refresh rate (2 second intervals)");
 *
 * update.setActionListener(new AjaxAdapter(){
 *     public Partial onAjaxAction(Control source) {
 *         Taconite partial = new Taconite();
 *
 *         // Execute raw JavaScript using eval
 *         partial.eval("Click.refresh.update('" + refreshId + "', 2000);");
 *         return partial;
 *     }
 * }); </pre>
 *
 * @author Bob Schellink
 */
public class JQRefreshHelper extends JQHelper {

    // -------------------------------------------------------------- Variables

    /**
     * The path of the template to render:
     * "<tt>/clickclick/jquery/template/jquery.refresh.template.js</tt>".
     */
    private String template = "/clickclick/jquery/template/jquery.refresh.template.js";

    private String refreshId;

    private int interval;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a JQRefreshHelper for the given target control.
     * <p/>
     * You need to set an {@link #interval} and {@link #refreshId} for
     * Ajax request to be executed.
     *
     * @param control the helper target control
     */
    public JQRefreshHelper(Control control) {
        super(control);
        setTemplate(template);
    }

    /**
     * Create a JQRefreshHelper for the given target control and refreshID.
     * <p/>
     * You need to set an {@link #interval} for the Ajax request to be executed.
     *
     * @param control the helper target control
     * @param refreshId the refresh ID
     */
    public JQRefreshHelper(Control control, String refreshId) {
        super(control);
        this.refreshId = refreshId;
        setTemplate(template);
    }

    /**
     * Create a JQRefreshHelper for the given target control and refreshID.
     *
     * @param control the helper target control
     * @param refreshId the refresh ID
     * @param interval the time interval between Ajax requests
     */
    public JQRefreshHelper(Control control, String refreshId, int interval) {
        super(control);
        this.refreshId = refreshId;
        this.interval = interval;
        setTemplate(template);
    }

    public Map createDefaultModel() {
        Map map = super.createDefaultModel();
        map.put("refreshId", getRefreshId());
        map.put("interval", getInterval());
        return map;
    }

    public String getRefreshId() {
        return refreshId;
    }

    public void setRefreshId(String refreshId) {
        this.refreshId = refreshId;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
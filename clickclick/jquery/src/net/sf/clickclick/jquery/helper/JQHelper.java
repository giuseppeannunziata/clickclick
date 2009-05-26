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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.click.ActionListener;
import net.sf.clickclick.AjaxControlRegistry;
import net.sf.clickclick.util.AjaxUtils;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.service.ConfigService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide a JQuery helper that Ajax enables a target control object.
 * <p/>
 * This helper has an associated JavaScript template that can be modified
 * according to your needs. Click <a href="../../../../../js/template/jquery.template.js.txt">here</a>
 * to view the template.
 * <p/>
 * JQHelper can either be embedded inside Click controls or used to decorate
 * the control.
 *
 * <h3>Embedded example</h3>
 *
 * Below is an example of a custom control with an embedded JQHelper that
 * enables Ajax behavior:
 *
 * <pre class="prettyprint">
 * public class JQActionLink extends AjaxActionLink {
 *
 *     // The embedded JQuery helper object.
 *     private JQHelper jqHelper = new JQHelper(this);
 *
 *     // Constructor
 *     public JQActionLink(String name) {
 *         super(name);
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
 * Below is an example how to decorate a TextField control to update a span
 * element when the user types into the textfield:
 *
 * <pre class="prettyprint">
 * public class FieldDemo extends BorderPage {
 *
 *     private Field field = new TextField("field");
 *     private Span label = new Span("label", "label");
 *
 *     public FieldDemo() {
 *
 *         // Register an Ajax listener on the field which is invoked on every
 *         // "keyup" event.
 *         field.setActionListener(new AjaxAdapter() {
 *             public Partial onAjaxAction(Control source) {
 *                 Taconite partial = new Taconite();
 *
 *                 // Set the label content to the latest field value
 *                 label.add(new Text(field.getValue()));
 *
 *                 // Replace the label in the browser with the new one
 *                 partial.replace(label);
 *                 return partial;
 *             }
 *         });
 *
 *         JQHelper helper = new JQHelper(field);
 *
 *         // Switch off the Ajax busy indicator
 *         helper.setShowIndicator(false);
 *
 *         // Delay Ajax invoke for 350 millis, otherwise too many calls are made
 *         // to the server
 *         helper.setThreshold(350);
 *
 *         // Set Ajax to fire on keyup events
 *         helper.setEvent("keyup");
 *
 *         // Ajaxify the the Field
 *         helper.ajaxify();
 *
 *         addControl(field);
 *         addControl(label);
 *     }
 * } </pre>
 *
 * @author Bob Schellink
 */
public class JQHelper {

    // -------------------------------------------------------------- Constants

    /**
     * The JQuery library (http://jquery.com/):
     * "<tt>/clickclick/jquery/jquery-1.3.2.js</tt>".
     */
    public static String jqueryImport = "/clickclick/jquery/jquery-1.3.2.js";

    /**
     * The JQuery Click library:
     * "<tt>/clickclick/jquery/jquery.click.js</tt>"
     * <p/>
     * This library includes JQuery Taconite plugin
     * (http://www.malsup.com/jquery/taconite/), JQuery LiveQuery plugin
     * (http://docs.jquery.com/Plugins/livequery)
     * and utility JavaScript functions.
     */
    public static String jqueryClickImport = "/clickclick/jquery/jquery.click.js";

    /**
     * The JQuery blockUI plugin (http://malsup.com/jquery/block/):
     * "<tt>/clickclick/jquery/blockui/jquery.blockUI.js</tt>".
     */
    public static String blockUIImport = "/clickclick/jquery/blockui/jquery.blockUI.js";

    /** The "<tt>onblur</tt>" event constant. */
    public static final String ON_BLUR = "blur";

    /** The "<tt>onchange</tt>" event constant. Ideal for Select controls. */
    public static final String ON_CHANGE = "change";

    /** The "<tt>click</tt>" event constant. */
    public static final String ON_CLICK = "click";

    /**
     * The custom "<tt>domready</tt> event. This event is fired as soon as the
     * dom is ready.
     */
    public static final String ON_DOMREADY = null;

    /** The "<tt>ondblclick</tt>" event constant. */
    public static final String ON_DOUBLE_CLICK = "dblclick";

    /** The "<tt>onfocus</tt>" event constant. */
    public static final String ON_FOCUS = "focus";

    /** The "<tt>onkeydown</tt>" event constant. */
    public static final String ON_KEYDOWN = "keydown";

    /** The "<tt>onkeypress</tt>" event constant. */
    public static final String ON_KEYPRESS = "keypress";

    /** The "<tt>onkeyup</tt>" event constant. */
    public static final String ON_KEYUP = "keyup";

    /** The "<tt>onload</tt>" event constant. */
    public static final String ON_LOAD = "load";

    /** The "<tt>onmousedown</tt>" event constant. */
    public static final String ON_MOUSEDOWN = "mousedown";

    /** The "<tt>onmouseenter</tt>" event constant. */
    public static final String ON_MOUSEENTER = "mouseenter";

    /** The "<tt>onmouseleave</tt>" event constant. */
    public static final String ON_MOUSELEAVE = "mouseleave";

    /** The "<tt>onmousemove</tt>" event constant. */
    public static final String ON_MOUSEMOVE = "mousemove";

    /** The "<tt>onmouseout</tt>" event constant. */
    public static final String ON_MOUSEOUT = "mouseout";

    /** The "<tt>onmouseover</tt>" event constant. */
    public static final String ON_MOUSEOVER = "mouseover";

    /** The "<tt>onmouseup</tt>" event constant. */
    public static final String ON_MOUSEUP = "mouseup";

    /** The "<tt>onresize</tt>" event constant. */
    public static final String ON_RESIZE = "resize";

    /** The "<tt>onscroll</tt>" event constant. */
    public static final String ON_SCROLL = "scroll";

    /** The "<tt>onselect</tt>" event constant. */
    public static final String ON_SELECT = "select";

    /** The "<tt>onsubmit</tt>" event constant. */
    public static final String ON_SUBMIT = "submit";

    /** The "<tt>onunload</tt>" event constant. */
    public static final String ON_UNLOAD = "unload";

    // -------------------------------------------------------------- Variables

    /**
     * The path of the default template to render:
     * "<tt>/clickclick/jquery/template/jquery.template.js</tt>".
     */
    private String template = "/clickclick/jquery/template/jquery.template.js";

    /**
     * The event which initiates an Ajax request, default value:
     * {@link #ON_CLICK}.
     */
    private String event = ON_CLICK;

    /** The data model for the JavaScript {@link #template}. */
    private Map model;

    /** The Ajax request parameters. */
    private Map parameters;

    /** The target control. */
    private Control control;

    /** The type request (POST / GET), default value is GET. */
    private String type = "GET";

    /** The Ajax request url. */
    private String url;

    /** The CSS selector for selecting the target element to Ajaxify. */
    private String selector;

    /**
     * The threshold within which multiple Ajax requests are merged into a
     * single request.
     */
    private int threshold = 0;

    /**
     * The message to display if an Ajax error occurs, default value:
     * "<tt>Error occurred</tt>".
     */
    private String errorMessage = "Error occurred";

    /**
     * Flag indicating whether an Ajax indicator (busy indicator) must be shown,
     * default value is true.
     */
    private boolean showIndicator = true;

    /**
     * The message to display when an Ajax indicator (busy indicator) is shown.
     */
    private String indicatorMessage;

    /**
     * The Ajax indicator (busy indicator) target.
     */
    private String indicatorTarget;

    /**
     * The Ajax indicator (busy indicator) options. See the JQuery
     * <a href="http://malsup.com/jquery/block/">BlockUI</a> plugin for
     * available options.
     */
    private String indicatorOptions;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new JQHelper for the given target control.
     *
     * @param control the helper target control
     */
    public JQHelper(Control control) {
        this.control = control;
    }

    /**
     * Create a new JQHelper for the given target control and CSS selector.
     * <p/>
     * Although any valid CSS selector can be specified, the CSS selector
     * usually specifies the HTML ID attribute of a target element on the page
     * eg: "<tt>#form-id</tt>".
     *
     * @param control the helper target control
     * @param selector the CSS selector
     */
    public JQHelper(Control control, String selector) {
        this.control = control;
        setSelector(selector);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the message to display when an error occurs during an Ajax request.
     * If no value is set, this method will try and lookup a localized message
     * using the target control for the key "<tt>ajax-error-message</tt>".
     * If a message cannot be found a default value is set:
     * "<tt>&;lt;h1&gt;Please wait...&lt;/h1&gt;</tt>".
     *
     * @return the message to display wnen an error occurs during an Ajax request
     */
    public String getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = getMessage("ajax-error-message");

            if (indicatorMessage == null) {
                // Set a default message
                indicatorMessage = "Error occurred!";
            }
        }
        return errorMessage;
    }

    /**
     * Set the error message to display when an error occurs during an Ajax
     * request.
     *
     * @param errorMessage the error message to display when an error occurs
     * during an Ajax request
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Return true if the Ajax indicator (busy indicator) should be shown,
     * false otherwise.
     *
     * @return true if the Ajax indicator (busy indicator) should be shown,
     * false otherwise
     */
    public boolean isShowIndicator() {
        return showIndicator;
    }

    /**
     * Set whether an Ajax indicator (busy indicator) should be shown during
     * Ajax requests.
     *
     * @param showIndicator indicates whether an Ajax indicator should be shown
     * during Ajax requests
     */
    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
    }

    /**
     * Return the number of milliseconds to wait before the Ajax request is
     * invoked.
     *
     * @see #setThreshold(int)
     *
     * @return the number of milliseconds to wait before the Ajax request is
     * invoked
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Set the number of milliseconds to wait before the Ajax request is
     * invoked, default value is 0, meaning requests are invoked immediately.
     * <p/>
     * <b>Please note:</b> all further Ajax requests invoked within the
     * threshold period are merged into a single request.
     *
     * @param threshold the threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Return the message to display when an Ajax indicator (busy indicator) is
     * shown. If no value is set, this method will try and lookup a
     * localized message using the target control for the key
     * "<tt>ajax-indicator-message</tt>". If a message cannot be found a
     * default value is set: "<tt>&;lt;h1&gt;Please wait...&lt;/h1&gt;</tt>".
     *
     * @return the message to display wnen an Ajax indicator is shown
     */
    public String getIndicatorMessage() {
        if (indicatorMessage == null) {
            indicatorMessage = getMessage("ajax-indicator-message");

            if (indicatorMessage == null) {
                // Set a default message
                indicatorMessage = "<h1>Please wait...</h1>";
            }
        }
        return indicatorMessage;
    }

    /**
     * Set the message to display when an Ajax indicator (busy indicator) is
     * shown. If no value is specified the the default value:
     * "<tt>&;lt;h1&gt;Please wait...&lt;/h1&gt;</tt>" is used.
     * <p/>
     * <b>Please note:</b> the indicator message will be enclosed inside single
     * quotes ('). If the message itself contains single quotes, they must be
     * escaped using two backslash (\\) characters e.g:
     * "<tt>Please enter your \\'name\\'.</tt>".
     *
     * @param indicatorMessage the message to display wnen an Ajax indicator is
     * shown
     */
    public void setIndicatorMessage(String indicatorMessage) {
        this.indicatorMessage = indicatorMessage;
    }

    /**
     * Return the target element that displays the Ajax indicator
     * (busy indicator).
     *
     * @return the target element that displays the Ajax indicator
     */
    public String getIndicatorTarget() {
        return indicatorTarget;
    }

    /**
     * Set the target element that displays the Ajax indicator (busy indicator).
     *
     * @param indicatorTarget the target element the displays the Ajax indicator
     */
    public void setIndicatorTarget(String indicatorTarget) {
        this.indicatorTarget = indicatorTarget;
    }

    /**
     * Set the target element that displays the Ajax indicator (busy indicator).
     *
     * @param indicatorTarget the target element the displays the Ajax indicator
     * @throws IllegalArgumentException if the control is null or the control ID
     * is not set
     */
    public void setIndicatorTarget(Control indicatorTarget) {
        if (indicatorTarget == null) {
            throw new IllegalArgumentException("control cannot be null");
        }
        String id = indicatorTarget.getId();
        if (id == null) {
            throw new IllegalArgumentException("control ID not set");
        }
        this.indicatorTarget = '#' + indicatorTarget.getId();
    }

    /**
     * Return the Ajax indicator (busy indicator) options.
     *
     * @see #setIndicatorOptions(java.lang.String)
     *
     * @return the target element that displays the Ajax indicator
     */
    public String getIndicatorOptions() {
        return indicatorOptions;
    }

    /**
     * Set the Ajax indicator (busy indicator) options.
     * <p/>
     * The Ajax indicator is based on the JQuery
     * <a href="http://malsup.com/jquery/block/">BlockUI</a> plugin so you can
     * use any of the options outlined
     * <a href="http://malsup.com/jquery/block/#options">here</a>.
     * <p/>
     * For example:
     *
     * <pre class="prettyprint">
     * public String getIndicatorOptions() {
     *     String options =
     *     "css : {"
     *         + "  textAlign: 'right',"
     *         + "  color: 'blue'"
     *         + "},"
     *         + "centerX: false,"
     *         + "centerY: false";
     *
     *     return options;
     * } </pre>
     *
     * <b>Please note</b> that the "<tt>message</tt>" option must be specified
     * through {@link #setIndicatorMessage(java.lang.String)} instead.
     *
     * @param indicatorOptions the Ajax indicator optiosn
     */
    public void setIndicatorOptions(String indicatorOptions) {
        this.indicatorOptions = indicatorOptions;
    }

    /**
     * Return the template to render for this helper.
     *
     * @return the template to render for this helper
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set the template to render for this helper.
     *
     * @param template the template to render for this helper
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Return the JavaScript event that fires the Ajax request.
     *
     * @return the JavaScript event that fires the Ajax request.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Set the JavaScript event that fires the Ajax request.
     *
     * @param event the JavaScript event that fires the Ajax request.
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Return the data model for the JavaScript {@link #template}.
     *
     * @return the data model for the JavaScript template
     */
    public Map getModel() {
        if (model == null) {
            model = createDefaultModel();
        }
        return model;
    }

    /**
     * Set the data model for the JavaScript {@link #template}.
     *
     * @param model the data model for the JavaScript template
     */
    public void setModel(Map model) {
        this.model = model;
    }

    /**
     * Return the target control that initiates the Ajax request.
     *
     * @return the target control that initiates the Ajax request
     */
    public Control getControl() {
        return control;
    }

    /**
     * Set the target control that initiates the Ajax request.
     *
     * @param control the target control that initiates the Ajax request
     */
    public void setControl(Control control) {
        this.control = control;
    }

    /**
     * Return the Ajax request parameter Map.
     *
     * @return the Ajax request parameter Map
     */
    public Map getParameters() {
        if (parameters == null) {
            parameters = new HashMap(2);
        }
        return parameters;
    }

    /**
     * Return true if the Ajax request has parameters, false otherwise.
     *
     * @return true if the Ajax request has parameters, false otherwise
     */
    public boolean hasParameters() {
        if (parameters != null && !parameters.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the Ajax request parameter.
     *
     * @param parameters the Ajax request parameters
     */
    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    /**
     * Set an Ajax request parameter with the given name and value.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    public void setParameter(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (value != null) {
            getParameters().put(name, value);
        } else {
            getParameters().remove(name);
        }
    }

    /**
     * Return the type of Ajax request eg GET or POST.
     *
     * @return the type of Ajax request
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the Ajax reques eg GET or POST.
     *
     * @param type the type of the Ajax request
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return the URL for the Ajax request, defaults to the URL of the
     * current Page.
     *
     * @return the URL for the Ajax request
     */
    public String getUrl() {
        if (url == null) {
            Context context = getContext();
            url = ClickUtils.getRequestURI(context.getRequest());
            url = context.getResponse().encodeURL(url);
        }
        return url;
    }

    /**
     * Set the URL for the Ajax request. If no URL is set it will default to
     * the URL of the current Page.
     *
     * @param url the URL for the Ajax request
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Return the CSS selector for selecting the target element to Ajaxify,
     * defaults to the ID attribute of the target {@link #control}.
     *
     * @return the CSS selector for selecting the target element to Ajaxify
     */
    public String getSelector() {
        if (selector == null) {
            Control control = getControl();
            selector = AjaxUtils.getSelector(control);
        }
        return selector;
    }

    /**
     * Set the CSS selector for selecting the target element to Ajaxify.
     * <p/>
     * Usually the selector will be the ID attribute of the target {@link #control}
     * eg "<tt>#my-field</tt>".
     *
     * @param selector the CSS selector for selecting the target element to
     * Ajaxify
     */
    public void setSelector(String selector) {
        this.selector = selector;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the localized message for the given key, or null if not found.
     * <p/>
     * This method will attempt to lookup the localized message in the
     * {@link #getControl()} parent's messages, which resolves to the Page's
     * resource bundle.
     * <p/>
     * If the message was not found, this method will attempt to look up the
     * value in the <tt>/click-control.properties</tt> message properties file,
     * through the method {@link #getMessages()}.
     * <p/>
     * If still not found, this method will return null.
     *
     * @param name the name of the message resource
     * @return the named localized message, or null if not found
     */
    public String getMessage(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        String message = null;

        message = ClickUtils.getParentMessage(getControl(), name);

        if (message == null && getMessages().containsKey(name)) {
            message = (String) getMessages().get(name);
        }

        return message;
    }

    /**
     * Return the formatted message for the given resource name,
     * message format argument and the context request locale, or null
     * if no message was found.
     * <p/>
     * {@link #getMessage(java.lang.String, java.lang.Object[])} is invoked to
     * retrieve the message for the specified name.
     *
     * @param name resource name of the message
     * @param arg the message argument to format
     * @return the named localized message for the control
     */
    public String getMessage(String name, Object arg) {
        Object[] args = new Object[] { arg };
        return getMessage(name, args);
    }

    /**
     * Return the formatted message for the given resource name,
     * message format arguments and the context request locale, or null if
     * no message was found.
     * <p/>
     * {@link #getMessage(java.lang.String)} is invoked to retrieve the message
     * for the specified name.
     *
     * @param name resource name of the message
     * @param args the message arguments to format
     * @return the named localized message for the package or null if no message
     * was found
     */
    public String getMessage(String name, Object[] args) {
        if (args == null) {
            throw new IllegalArgumentException("Null args parameter");
        }
        String value = getMessage(name);
        if (value == null) {
            return null;
        }
        return MessageFormat.format(value, args);
    }

    /**
     * Return a Map of localized messages for the specified {@link #getControl()}.
     *
     * @return a Map of localized messages for the control
     * @throws IllegalStateException if the context for the control has not be set
     */
    public Map getMessages() {
        return getControl().getMessages();
    }

    /**
     * Create a default data model for the Ajax {@link #template}.
     * <p/>
     * The following values are added:
     * <ul>
     * <li>"{@link #context}" - the request context path e.g: '/myapp'</li>
     * <li>"{@link #control}" - the target control</li>
     * <li>"{@link #selector}" - the CSS selector</li>
     * <li>"{@link #event}" - the event that initiates the Ajax request</li>
     * <li>"<span color="blue">productionMode</span>" - true if Click is running
     * in a production mode (production or profile), false otherwise</li>
     * <li>"{@link #url url}" - the Ajax request URL</li>
     * <li>"{@link #type}" - the type of the Ajax request, eg POST or GET</li>
     * <li>"{@link #threshold}" - the threshold within which multiple Ajax
     * requests are merged into a single request.</li>
     * <li>"{@link #showIndicator}" - the showIndicator flag</li>
     * <li>"{@link #indicatorOptions}"</span> - the Ajax indicator options. Note
     * that {@link #indicatorMessage} is rendered as part of the options</li>
     * <li>"{@link #indicatorTarget}" - the target element of the Ajax indicator</li>
     * <li>"{@link #errorMessage}" - the message to display if an Ajax error occurs</li>
     * <li>"{@link #parameters}" - the Ajax request parameters</li>
     * <li><span color="blue">"selector"</span> - the CSS {@link #selector}</li>
     * </ul>
     *
     * @return the default data model for the Ajax template
     */
    public Map createDefaultModel() {
        Context context = getContext();
        ConfigService configService = ClickUtils.getConfigService(context.getServletContext());
        boolean productionMode = configService.isProductionMode()
            || configService.isProfileMode();

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        String message = getIndicatorMessage();
        String options = getIndicatorOptions();

        if (message != null) {
            buffer.append("message:'").append(message).append("'");
        }

        if (options != null) {
            if (buffer.length() > 0) {
                buffer.append(",");
            }
            buffer.append(options);
        }

        Map model = new HashMap();
        model.put("context", context.getRequest().getContextPath());
        model.put("control", getControl());
        model.put("selector", getSelector());
        model.put("event", getEvent());
        model.put("productionMode", productionMode ? "true" : "false");
        model.put("url", getUrl());
        model.put("type", getType());
        model.put("threshold", getThreshold());
        model.put("showIndicator", isShowIndicator() ? "true" : "false");
        model.put("indicatorOptions", buffer.toString());
        model.put("indicatorTarget", getIndicatorTarget());
        model.put("errorMessage", getErrorMessage());
        model.put("parameters", serializeParameters());
        return model;
    }

    /**
     * Ajaxifies the the target {@link #control} so that its registered
     * {@link net.sf.clickclick.AjaxListener} can be invoked for Ajax requests.
     * <p/>
     * This method does the following:
     * <ul>
     * <li>Regsiters the target {@link control} on the {@link net.sf.clickclick.AjaxControlRegistry}
     * by invoking {@link net.sf.clickclick.AjaxControlRegistry#registerAjaxControl(Control)}</li>
     * <li>invokes {@link #addHeadElements(java.util.List)} which adds the necessary
     * JavaScript imports and scripts to enable Ajax requests</li>
     * </ul>
     */
    public void ajaxify() {
        // Reason we register a callback below is that the control ID might only
        // be fuly set after #ajaxify is invoked. By using the special
        // ON_AJAX_EVENT callback which is triggered before onProcess, we
        // ensure that the control ID will be available. Also by adding HEAD
        // elements in this event, Controls inside containers such as Repeaters
        // can still be Ajax targets because their names, at this stage, still
        // have the Repeater index applied.
        AjaxControlRegistry.registerActionEvent(getControl(), new ActionListener() {
            public boolean onAction(Control source) {
                AjaxControlRegistry.registerAjaxControl(source);
                addHeadElements(getControl().getHeadElements());
                return true;
            }
        }, AjaxControlRegistry.ON_AJAX_EVENT);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the Context of the current request
     *
     * @return the Context of the current request
     */
    protected Context getContext() {
        return Context.getThreadLocalContext();
    }

    /**
     * Return the Ajax request {@link #parameters} as a serialized URL string.
     * <p/>
     * The serialized string will consist of name/value pairs delimited by
     * the '&' char. An example URL string could be:
     * "<tt>firstname=John&lastname=Smith&age=12</tt>".
     *
     * @return the Ajax request parameters as a serialized URL string
     */
    protected String serializeParameters() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        Iterator i = getParameters().keySet().iterator();
        while (i.hasNext()) {
            Object key = i.next();
            if (key == null) {
                continue;
            }
            String name = key.toString();
            Object paramValue = getParameters().get(name);
            String encodedValue =
                ClickUtils.encodeURL(paramValue);
            buffer.append(name);
            buffer.append("=");
            buffer.append(encodedValue);
            if (i.hasNext()) {
                buffer.append("&");
            }
        }
        return buffer.toString();
    }

    /**
     * Add the necessary JavaScript imports and scripts to the given
     * headElements list to enable Ajax requests.
     *
     * @param headElements the list which to add all JavaScript imports and
     * scripts to enable Ajax requests
     */
    protected void addHeadElements(List headElements) {
        JsImport jsImport = new JsImport(jqueryImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(0, jsImport);
        }

        jsImport = new JsImport(jqueryClickImport);
        if (!headElements.contains(jsImport)) {
            headElements.add(1, jsImport);
        }

        if (isShowIndicator()) {
            jsImport = new JsImport(blockUIImport);
            if (!headElements.contains(jsImport)) {
                headElements.add(2, jsImport);
            }
        }

        ServletContext servletContext = getContext().getServletContext();
        ConfigService configService = ClickUtils.getConfigService(servletContext);

        // If Click is running in development modes, enable JavaScript debugging
        if (!configService.isProductionMode() && !configService.isProfileMode()) {
            addJSDebugScript(headElements);
        }
        addTemplate(headElements);
    }

    /**
     * Add a special {@link org.apache.click.element.JsScript} which enables
     * detailed JavaScript log output to the given headElements list.
     * <p/>
     * <b>Please note:</b> use the Firefox browser and Firebug plugin to view
     * the logged output. However other browsers might also support logging
     * output.
     *
     * @param headElements list which to add the debug script to
     */
    protected void addJSDebugScript(List headElements) {
        JsScript jsScript = new JsScript();
        jsScript.append("if (typeof jQuery != 'undefined') {\n");
        jsScript.append("  if (typeof jQuery.taconite != 'undefined') {\n");
        jsScript.append("    jQuery.taconite.debug = true;\n");
        jsScript.append("  }\n");
        jsScript.append("  if (typeof Click != 'undefined') {\n");
        jsScript.append("    Click.debug = true;\n");
        jsScript.append("  }\n");
        jsScript.append("}");

        jsScript.setId("enable_js_debugging");
        headElements.add(jsScript);
    }

    /**
     * Add the {@link #template} content to the given headElements list.
     *
     * @param headElements list which to add the Ajax template to
     */
    protected void addTemplate(List headElements) {

        if (StringUtils.isNotBlank(getTemplate())) {
            JsScript jsScript = new JsScript(getTemplate(), getModel());
            String id = control.getId();
            if (StringUtils.isBlank(id)) {
                 id = control.getName();
            }
            jsScript.setId(id + "_jquery_template");

            // remove previous script in case of stateful pages
            headElements.remove(jsScript);
            // add script
            headElements.add(jsScript);
        }
    }
}

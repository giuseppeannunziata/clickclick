/*
 * Copyright 2004-2008 Malcolm A. Edgar
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
package net.sf.clickclick.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.Format;
import org.apache.click.util.MessagesMap;
import org.apache.click.util.SessionMap;

/**
 *
 * @author Bob Schellink
 */
public class ClickClickUtils {

    // -------------------------------------------------------------- Constants

    /** The cached application version indicator string. */
    private static String cachedApplicationVersionIndicator;

    /** The static web resource version number indicator string. */
    public static final String APPLICATION_VERSION_INDICATOR =
        "_" + getApplicationVersion();

    // --------------------------------------------------------- Public Methods

    /**
     * Creates a template model of key/value pairs which can be used by template
     * engines such as Velocity or Freemarker.
     * <p/>
     * The following objects will be added to the model:
     * <ul>
     *   <li>context - the Servlet context path, e.g. <span class="">/mycorp</span>
     *   </li>
     *   <li>format - the {@link Format} object for formatting the display of
     *     objects.
     *   </li>
     *   <li>messages - the {@link MessagesMap} adaptor for the
     *     {@link net.sf.click.Page#getMessages()} method.
     *   </li>
     *   <li>path - the {@link net.sf.click.Page#path} of the <tt>page</tt>
     *     template.
     *   </li>
     *   <li>request - the pages {@link javax.servlet.http.HttpServletRequest}
     *     object.
     *   </li>
     *   <li>response - the pages {@link javax.servlet.http.HttpServletResponse}
     *     object.
     *   </li>
     *   <li>session - the {@link SessionMap} adaptor for the users
     *     {@link javax.servlet.http.HttpSession}.
     *   </li>
     * </ul>
     *
     * @param page the page used to populate the template model
     * @param context the request context
     * @return a template model as a map
     */
    public static Map createTemplateModel(final Page page, Context context) {

        ConfigService configService = ClickUtils.getConfigService(context.getServletContext());
        LogService logger = configService.getLogService();

        final Map model = new HashMap(page.getModel());

        final HttpServletRequest request = page.getContext().getRequest();

        Object pop = model.put("request", request);
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"request\". The page model object "
                         + pop + " has been replaced with the request object";
            logger.warn(msg);
        }

        pop = model.put("response", page.getContext().getResponse());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"response\". The page model object "
                         + pop + " has been replaced with the response object";
            logger.warn(msg);
        }

        SessionMap sessionMap = new SessionMap(request.getSession(false));
        pop = model.put("session", sessionMap);
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"session\". The page model object "
                         + pop + " has been replaced with the request "
                         + " session";
            logger.warn(msg);
        }

        pop = model.put("context", request.getContextPath());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"context\". The page model object "
                         + pop + " has been replaced with the request "
                         + " context path";
            logger.warn(msg);
        }

        Format format = page.getFormat();
        if (format != null) {
            pop = model.put("format", format);
            if (pop != null && !page.isStateful()) {
                String msg = page.getClass().getName() + " on "
                        + page.getPath()
                        + " model contains an object keyed with reserved "
                        + "name \"format\". The page model object " + pop
                        + " has been replaced with the format object";
                logger.warn(msg);
            }
        }

        String path = page.getPath();
        if (path != null) {
           pop = model.put("path", path);
            if (pop != null && !page.isStateful()) {
                String msg = page.getClass().getName() + " on "
                        + page.getPath()
                        + " model contains an object keyed with reserved "
                        + "name \"path\". The page model object " + pop
                        + " has been replaced with the page path";
                logger.warn(msg);
            }
        }

        pop = model.put("messages", page.getMessages());
        if (pop != null && !page.isStateful()) {
            String msg = page.getClass().getName() + " on " + page.getPath()
                         + " model contains an object keyed with reserved "
                         + "name \"messages\". The page model object "
                         + pop + " has been replaced with the request "
                         + " messages";
            logger.warn(msg);
        }

        return model;
    }

    /**
     * Return the Application version string which is controlled by the property
     * <tt>application-version</tt> in the file <tt>click-control.properties</tt>.
     *
     * @return the Application version string
     */
    public static String getApplicationVersion() {
        ResourceBundle bundle = ClickUtils.getBundle("click-control");
        return bundle.getString("application-version");
    }

    /**
     * Return a version indicator for static web resources
     * (eg JavaScript and CSS) if resource versioning is active,
     * otherwise this method will return an empty string.
     * <p/>
     * The version indicator is based on the current Application release version.
     * For example if the application release number is 1.2 this method will
     * return the string <tt>"-1.2"</tt>.
     * <p/>
     * The application version can be specified by the property
     * <tt>application-version</tt> in the file <tt>click-control.properties</tt>.
     * Alternatively you can manually set the
     * {@link #cachedApplicationVersionIndicator}.
     *
     * @param context the request context
     * @return a version indicator for web resources
     */
    public static String getApplicationResourceVersionIndicator(Context context) {
        if (cachedApplicationVersionIndicator != null) {
            return cachedApplicationVersionIndicator;
        }

        ConfigService configService = ClickUtils.getConfigService(context.getServletContext());

        boolean isProductionModes = configService.isProductionMode()
            || configService.isProfileMode();

        if (isProductionModes && ClickUtils.isEnableResourceVersion(context)) {

            cachedApplicationVersionIndicator = APPLICATION_VERSION_INDICATOR;
            return cachedApplicationVersionIndicator;

        } else {
            return "";
        }
    }
}

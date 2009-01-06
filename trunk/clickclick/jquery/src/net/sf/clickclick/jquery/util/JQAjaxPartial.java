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
package net.sf.clickclick.jquery.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.click.Context;
import net.sf.click.util.Partial;

/**
 *
 * @author Bob Schellink
 */
public class JQAjaxPartial extends Partial {

    private boolean noConflict = true;

    private boolean replaceTarget;

    private String targetId;

    private String focusId;

    private String redirect;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct the Partial for the given content and content type.
     * <p/>
     * At rendering time the partial invokes the Object's <tt>toString()</tt>
     * method and streams the resulting <tt>String</tt> back to the client.
     *
     * @param content the content to stream back to the client
     * @param contentType the response content type
     */
    public JQAjaxPartial(Object content, String contentType) {
        super(content, contentType);
    }

    /**
     * Construct the Partial for the given content. The
     * <tt>{@link javax.servlet.http.HttpServletResponse#setContentType(java.lang.String) response content type}</tt>
     * will default to {@link #TEXT}.
     * <p/>
     * At rendering time the partial invokes the Object's <tt>toString()</tt>
     * method and streams the resulting <tt>String</tt> back to the client.
     *
     * @param content the content to stream back to the client
     */
    public JQAjaxPartial(Object content) {
        super(content);
    }

    /**
     * Construct a new empty Partial.
     */
    public JQAjaxPartial() {
    }

    // ---------------------------------------------------- Getters and Setters

    public boolean isReplaceTarget() {
        return replaceTarget;
    }

    public void setReplaceTarget(boolean replaceTarget) {
        this.replaceTarget = replaceTarget;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getFocusId() {
        return focusId;
    }

    public void setFocusId(String focusId) {
        this.focusId = focusId;
    }

    public void setNoConflict(boolean noConflict) {
        this.noConflict = noConflict;
    }

    public boolean isNoConflict() {
        return noConflict;
    }

    public String getRedirect() {
        return redirect;
    }

    /**
     * Set the location to redirect the request to.
     * <p/>
     * If the {@link #redirect} property is not null it will be used to redirect
     * the request in preference to {@link #forward} or {@link #path} properties.
     * The request is redirected to using the HttpServletResponse.setRedirect()
     * method.
     * <p/>
     * If the redirect location begins with a <tt class="wr">"/"</tt>
     * character the redirect location will be prefixed with the web applications
     * context path. Also if the location has a <tt>.jsp</tt> extension it will
     * be changed to <tt>.htm</tt>.
     * <p/>
     * For example if an application is deployed to the context
     * <tt class="wr">"mycorp"</tt> calling
     * <tt>setRedirect(<span class="navy">"/customer/details.htm"</span>)</tt>
     * will redirect the request to:
     * <tt class="wr">"/mycorp/customer/details.htm"</tt>
     * <p/>
     * See also {@link #setForward(String)}, {@link #setPath(String)}
     *
     * @param location the path to redirect the request to
     */
    public void setRedirect(String location) {
        redirect = location;
    }

    /**
     * Set the request to redirect to the give page class.
     *
     * @param pageClass the class of the Page to redirect the request to
     * @throws IllegalArgumentException if the Page Class is not configured
     * with a unique path
     */
    public void setRedirect(Class pageClass) {
        String target = Context.getThreadLocalContext().getPagePath(pageClass);
        setRedirect(target);
    }

    /**
     * Render the partial to the specified response.
     *
     * @param request the page servlet request
     * @param response the page servlet response
     */
    public void render(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Click.replace", isReplaceTarget() ? "true" : "false");
        response.setHeader("Click.ajaxTargetId", getTargetId());
        response.setHeader("Click.focusId", getFocusId());
        if (getRedirect() != null && getRedirect().trim().length() > 0) {
            response.setHeader("Click.redirectUrl", createValidRedirect(getRedirect()));
        }
        super.render(request, response);
    }

    private String createValidRedirect(String url) {
        Context context = Context.getThreadLocalContext();
        String contextPath = context.getRequest().getContextPath();
        if (url.charAt(0) == '/') {
            url = contextPath + url;

            // Check for two scenarios, one without parameters and one with:
            // #1. /context/my-page.jsp
            // #2. /context/my-page.jsp?param1=value&param2=other-page.jsp
            if (url.endsWith(".jsp")) {
                url = url.replaceFirst(".jsp", ".htm");
            } else if (url.indexOf(".jsp?") >= 0) {
                url = url.replaceFirst(".jsp?", ".htm?");
            }
        }

        return context.getResponse().encodeRedirectURL(url);
    }
}

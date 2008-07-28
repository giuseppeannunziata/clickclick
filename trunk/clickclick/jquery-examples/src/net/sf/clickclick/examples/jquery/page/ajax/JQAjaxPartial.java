package net.sf.clickclick.examples.jquery.page.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        super.render(request, response);
    }
}

package net.sf.clickclick.control.breadcrumb;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.click.MockContext;
import org.apache.click.control.AbstractControl;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * This Control is an implementation of a breadcrumb or history bar.
 *
 * Correct usage of breadcrumb is:
 *
 * http://www.useit.com/alertbox/breadcrumbs.html
 */
public class Breadcrumb extends AbstractControl {

    // -------------------------------------------------------- Constants

    public static final String BREADCRUMB_KEY = "breadcrumb";

    // -------------------------------------------------------- Instance Variables

    private int maxTrailLength = 5;

    private String seperator = " / ";

    private transient Trail trail;

    private String label;

    private transient Set excludedPaths = new HashSet();
    
    // -------------------------------------------------------- Constructors

    public Breadcrumb() {
    }

    public Breadcrumb(String name) {
        setName(name);
    }

    public Breadcrumb(String name, String label) {
        setName(name);
        setLabel(label);
    }

    public Breadcrumb(String name, int maxTrailLengthArg) {
        this(name, null, maxTrailLengthArg, " / ");
    }

    public Breadcrumb(String name, String label, int maxTrailLengthArg) {
        this(name, label, maxTrailLengthArg, " / ");
    }

    public Breadcrumb(String name, String label, int maxTrailLengthArg, String seperatorArg) {
        setName(name);
        setLabel(label);
        setMaxTrailLength(maxTrailLengthArg);
        setSeperator(seperatorArg);
    }

    // -------------------------------------------------------- Public Getters/Setters

    public int getMaxTrailLength() {
        return maxTrailLength;
    }

    public void setMaxTrailLength(int maxTrailLength) {
        this.maxTrailLength = maxTrailLength;
    }

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public Set getExcludedPaths() {
        return excludedPaths;
    }

    public void setLabel(Set excludedPaths) {
        this.excludedPaths = excludedPaths;
    }

    // -------------------------------------------------------- Implement Control

    public void onInit() {
        restoreState();
        String contextPath = getContext().getRequest().getContextPath();
        String path = ClickUtils.getResourcePath(getContext().getRequest());
        addTrail(contextPath + path);
    }

    public String getHtmlImports() {
        return null;
    }

    public void setListener(Object listener, String method) {
    }

    public void onDeploy(ServletContext servletContext) {
    }

    public void onRender() {
    }

    public void onDestroy() {
        saveState();
    }

    public boolean onProcess() {
        return true;
    }

    // -------------------------------------------------------- Public Methods

    public void addTrail(String path) {
        for(Iterator it = getExcludedPaths().iterator(); it.hasNext(); ) {
            String excludedPath = (String) it.next();
            if(path.indexOf(excludedPath) >= 0) {
                return;
            }
        }

        if (getTrail().containsKey(path)) {
            collapseTrail(path);
            return;
        }
        String pageName = extractPageName(path);
        getTrail().put(path, pageName);
    }

    public void removeTrail(String path) {
        getTrail().remove(path);
    }

    public int size() {
        return getTrail().size();
    }

    public void clear() {
        getTrail().clear();
    }

    public Map getTrail() {
        if (trail == null) {
            trail = new Trail(this);
        }
        return trail;
    }

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(getTrail().size() * 20);
        renderTrail(buffer);
        return buffer.toString();
    }

    // -------------------------------------------------------- Protected Methods
    protected void renderTrail(HtmlStringBuffer buffer) {
        buffer.elementStart("div");
        buffer.appendAttribute("id", getId());
        buffer.closeTag();
        if (getLabel() != null) {
            buffer.append("<span>");
            buffer.append(getLabel());
            buffer.append(" - ");
            buffer.append("</span>");
        }

        // Only render when there are at least 1 trail entries
        if (getTrail().size() > 0) {
            Iterator it = getTrail().entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();

                String path = (String) entry.getKey();
                String pageName = (String) entry.getValue();
                renderPath(path, pageName, !it.hasNext(), buffer);

                if (it.hasNext()) {
                    buffer.append("<span style=\"padding:0 2px;\">");
                    buffer.append(getSeperator());
                    buffer.append("</span>");
                }
            }
        }
        buffer.append("</div>");
    }

    protected void renderPath(String path, String pageName, boolean isLastEntry, HtmlStringBuffer buffer) {

        // If its the last entry only render a string not a hyperlink.
        if (isLastEntry) {
            buffer.append(pageName);
            return;
        }

        buffer.elementStart("a");

        buffer.appendAttribute("href", path);

        // Append all attributes
        appendAttributes(buffer);

        buffer.closeTag();

        buffer.append(pageName);

        buffer.elementEnd("a");
    }

    protected String extractPageName(String path) {

        // We start off optimistic
        String pagePath = path;

        // Extract the page path
        int pageIndex = pagePath.lastIndexOf("/");
        if (pageIndex != -1) {
            pagePath = pagePath.substring(pageIndex + 1);
        }

        // Chop off the page extension eg. ".htm"
        int extensionIndex = pagePath.lastIndexOf(".");
        if (extensionIndex != -1) {
            pagePath = pagePath.substring(0, extensionIndex);
        }
        return pagePath;
    }

    protected void collapseTrail(String newPath) {
        // Indicates if trail entries must be removed
        boolean remove = false;
        for (Iterator it = getTrail().keySet().iterator(); it.hasNext();) {
            String path = (String) it.next();

            if (remove) {
                // Remove the current trail entry and continue iterating
                it.remove();
                continue;
            }

            // Check if current trail path equals newPath
            if (path.equals(newPath)) {
                // Activate remove indicator to remove all remaining entries
                remove = true;
            }
        }
    }

    protected void restoreState() {
        Trail existingTrail = (Trail) getContext().getSession().getAttribute(BREADCRUMB_KEY);
        if (existingTrail == null) {
            return;
        }
        this.trail = existingTrail;
        this.trail.setBreadcrumb(this);
        
    }

    protected void saveState() {
        HttpSession session = getContext().getRequest().getSession(false);
        if (session != null) {
            session.setAttribute(BREADCRUMB_KEY, trail);
        }
    }

    public static void main(String[] args) {
        MockContext.initContext();
        Breadcrumb crumb = new Breadcrumb("bc", "bread crumb label:", 6, " / ");

        String link = "path1";
        crumb.addTrail(link);
        link = "context/path2";
        crumb.addTrail(link);
        link = "context/path2";
        crumb.addTrail(link);
        link = "/context/path3.htm?param1=one&param2=two";
        crumb.addTrail(link);
        System.out.println(crumb);
    }
}

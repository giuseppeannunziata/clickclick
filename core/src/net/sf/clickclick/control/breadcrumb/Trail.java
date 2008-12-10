package net.sf.clickclick.control.breadcrumb;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Bob Schellink
 */
public class Trail extends LinkedHashMap {

    private transient Breadcrumb breadcrumb;

    public Trail(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > getBreadcrumb().getMaxTrailLength();
    }

    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }
}

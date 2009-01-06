package net.sf.clickclick.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 * @author Bob Schellink
 */
public class HeadEntries extends ArrayList {

    public void add(int index, Object htmlImport) {
        if (htmlImport == null) {
            throw new IllegalArgumentException("Import cannot be null");
        }
        if (!(htmlImport instanceof Head)) {
            String msg = "Not a valid Import type: " + htmlImport.getClass().getName();
            throw new IllegalArgumentException(msg);
        }
        super.add(index, htmlImport);
    }

    public boolean add(Object htmlImport) {
        add(size(), htmlImport);
        return true;
    }

    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException("addAll not supported. use add instead");
    }

    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException("addAll not supported. use add instead");
    }

    public Object set(int index, Object element) {
        throw new UnsupportedOperationException("set not supported. use add instead");
    }

    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(size() * 100);
        Iterator it = iterator();
        while(it.hasNext()) {
            Head imp = (Head) it.next();
            buffer.append(imp.toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }
    
    public static void main(String[] args) {        
        HeadEntries list = new HeadEntries();
        list.add(new JavascriptImport());
        System.out.println(list);
    }
}

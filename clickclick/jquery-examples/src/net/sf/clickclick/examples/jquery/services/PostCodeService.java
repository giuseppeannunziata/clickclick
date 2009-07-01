package net.sf.clickclick.examples.jquery.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.sf.clickclick.examples.jquery.domain.PostCode;
import net.sf.clickclick.examples.jquery.util.StartupListener;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Bob Schellink
 */
public class PostCodeService {

    public List<PostCode> getPostCodes() {
        return StartupListener.POST_CODES;
    }

    public List<String> getPostCodeLocations(String location) {
        List list = new ArrayList();

        for(PostCode postCode : getPostCodes()) {
            if (StringUtils.startsWithIgnoreCase(postCode.getLocality(), location)) {
                String value = postCode.getLocality() + ", " + postCode.getState() +
                    " " + postCode.getPostCode();
                list.add(value);
            }
        }

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return list;
    }
}

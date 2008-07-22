package net.sf.clickclick.examples.page.reload;

import net.sf.clickclick.examples.page.BorderPage;

/**
 *
 * @author Bob Schellink
 */
public class ResourceBundlePage extends BorderPage {
    
    private String myproperty;
    
    public void onInit() {
        myproperty = getMessage("myproperty");
        addModel("myproperty", myproperty);
    }
}

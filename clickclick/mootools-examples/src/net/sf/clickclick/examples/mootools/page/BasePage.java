package net.sf.clickclick.examples.mootools.page;

import org.apache.log4j.Logger;

import org.apache.click.Page;

/**
 *  Provides the base page with include business services, which
 *  other application pages should extend.
 */
public class BasePage extends Page {

    private Logger logger;

    /**
     * Return the class logger.
     *
     * @return the class logger
     */
    public Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(getClass());
        }
        return logger;
    }

}

package net.sf.clickclick.examples.jquery.services;

/**
 *
 * @author Bob Schellink
 */
public class ApplicationRegistry {

    // -------------------------------------------------------------- Variables

    private CustomerService customerService;

    private PostCodeService postCodeService;

    // -------------------------------------------------------- Constructors

    private ApplicationRegistry() {
    }

    public static ApplicationRegistry getInstance() {
        return SingletonHolder.instance;
    }

    // --------------------------------------------------------- Public Methods

    public CustomerService getCustomerService() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    public PostCodeService getPostCodeService() {
        if (postCodeService == null) {
            postCodeService = new PostCodeService();
        }
        return postCodeService;
    }

    // ---------------------------------------------------------- Inner Classes

    // Class holder lazy initialization technique
    private static class SingletonHolder {
        public static ApplicationRegistry instance = new ApplicationRegistry();
    }
}

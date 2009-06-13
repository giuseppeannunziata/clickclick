package net.sf.clickclick.examples.jquery.services;

import java.util.List;
import net.sf.clickclick.examples.jquery.domain.Customer;
import net.sf.clickclick.examples.jquery.util.StartupListener;

/**
 *
 * @author Bob Schellink
 */
public class CustomerService {

    public List<Customer> getCustomers() {
        return StartupListener.CUSTOMERS;
    }
}

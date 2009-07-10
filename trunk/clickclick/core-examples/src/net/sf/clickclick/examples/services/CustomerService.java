package net.sf.clickclick.examples.services;

import java.util.List;
import net.sf.clickclick.examples.domain.Customer;
import net.sf.clickclick.examples.util.StartupListener;

/**
 *
 */
public class CustomerService {

    public List<Customer> getCustomers() {
        return StartupListener.CUSTOMERS;
    }
}

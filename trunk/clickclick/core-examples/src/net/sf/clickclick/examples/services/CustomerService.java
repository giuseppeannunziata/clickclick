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

    public Customer findCustomer(Object id) {
        for (Customer customer : getCustomers()) {
            if (customer.getId().toString().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    public Customer createCustomer() {
        Customer customer = new Customer();
        List<Customer> customers = getCustomers();
        Long prevId = customers.get(customers.size() - 1).getId();
        customer.setId(prevId + 1);
        return customer;
    }
}

package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Customer getCustomersById(UUID id);
    Customer addCustomer(Customer customer);
    Customer deleteCustomerById(UUID id);
    void updateCustomer(UUID id, Customer customer);
}

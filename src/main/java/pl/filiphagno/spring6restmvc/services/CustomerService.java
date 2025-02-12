package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Optional<Customer> getCustomersById(UUID id);
    Customer addCustomer(Customer customer);
    Optional<Customer> deleteCustomerById(UUID id);
    void updateCustomer(UUID id, Customer customer);
}

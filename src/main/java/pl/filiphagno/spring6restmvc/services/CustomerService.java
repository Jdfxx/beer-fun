package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomersById(UUID id);
    CustomerDTO addCustomer(CustomerDTO customerDTO);
    void deleteCustomerById(UUID id);
    Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO);
}

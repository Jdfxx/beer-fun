package pl.filiphagno.spring6restmvc.services;

import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.mappers.CustomerMapper;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;
import pl.filiphagno.spring6restmvc.repositories.CustomerRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, CustomerDTO> customers;
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customers = new HashMap<>();
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO addCustomer(CustomerDTO customerDTO) {

        CustomerDTO newCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .name(customerDTO.name())
                .version(customerDTO.version())
                .build();
        customers.put(newCustomerDTO.id(), newCustomerDTO);

        return newCustomerDTO;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomersById(UUID id) {
        return Optional.ofNullable(customers.get(id));
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO customerDTO) {
        CustomerDTO oldCustomerDTO = customers.get(id);
        CustomerDTO newCustomerDTO = CustomerDTO.builder()
                .id(oldCustomerDTO.id())
                .name(customerDTO.name())
                .created(oldCustomerDTO.created())
                .updated(LocalDateTime.now())
                .version(oldCustomerDTO.version() + 1)
                .build();
        customers.put(id, newCustomerDTO);
    }

    @Override
    public void deleteCustomerById(UUID id) {
         customerRepository.deleteById(id);
    }
}

package pl.filiphagno.spring6restmvc.services;

import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.entities.Customer;
import pl.filiphagno.spring6restmvc.mappers.CustomerMapper;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;
import pl.filiphagno.spring6restmvc.repositories.CustomerRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
        Customer savedCustomer = customerRepository
                .save(customerMapper.customerDtoToCustomer(customerDTO));
        return customerMapper.customerToCustomerDTO(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomersById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDTO(
                customerRepository.findById(id).orElse(null))
        );
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresentOrElse( existingCustomer -> {
            existingCustomer.setName(customerDTO.name());
            atomicReference.set(Optional.of(
                    customerMapper.customerToCustomerDTO(
                            customerRepository.save(existingCustomer))));
        }, ()-> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public void deleteCustomerById(UUID id) {
         customerRepository.deleteById(id);
    }


}

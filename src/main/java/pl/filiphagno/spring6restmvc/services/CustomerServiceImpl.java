package pl.filiphagno.spring6restmvc.services;

import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, CustomerDTO> customers;

    public CustomerServiceImpl() {
        this.customers = new HashMap<>();

        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .version(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .version(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Frank Doe")
                .version(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        customers.put(customerDTO1.id(), customerDTO1);
        customers.put(customerDTO2.id(), customerDTO2);
        customers.put(customerDTO3.id(), customerDTO3);

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
        return new ArrayList<>(customers.values());
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
    public Optional<CustomerDTO> deleteCustomerById(UUID id) {
        return Optional.ofNullable(customers.remove(id));
    }
}

package pl.filiphagno.spring6restmvc.services;

import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.model.Customer;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, Customer> customers;

    public CustomerServiceImpl() {
        this.customers = new HashMap<>();

        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .version(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .version(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Frank Doe")
                .version(1)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        customers.put(customer1.id(), customer1);
        customers.put(customer2.id(), customer2);
        customers.put(customer3.id(), customer3);

    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public Customer getCustomersById(UUID id) {
        return customers.get(id);
    }
}

package pl.filiphagno.spring6restmvc.intergration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import pl.filiphagno.spring6restmvc.controllers.CustomerController;
import pl.filiphagno.spring6restmvc.controllers.NotFoundException;
import pl.filiphagno.spring6restmvc.entities.Customer;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;
import pl.filiphagno.spring6restmvc.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomerControllerIntegrationTest {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Test
    void getAllCustomers() {
        List<CustomerDTO> customers = customerController.listCustomers();
        assertThat(customers.size()).isEqualTo(3);
    }

    @Transactional
    @Test
    void getAllCustomersEmpty() {
        customerRepository.deleteAll();
        List<CustomerDTO> customers = customerController.listCustomers();
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void getCustomerByExist() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerController.getCustomerBy(customer.getId());
        assertThat(customerDTO).isNotNull();
    }

    @Test
    void getCustomerByNotFound() {
        assertThrows(NotFoundException.class,
                ()-> customerController.getCustomerBy(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testAddCustomer() {
        CustomerDTO customer = CustomerDTO.builder()
                .name("testName")
                .build();
        ResponseEntity<CustomerDTO> responseEntity = customerController.addCustomer(customer);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID customerId = UUID.fromString(location[4]);
        Customer savedCustomer = customerRepository.findById(customerId).get();
        assertThat(savedCustomer).isNotNull();
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateCustomerExists()  {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("UPDATED")
                .build();
        customerController.updateCustomer(customer.getId(), customerDTO);
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getName()).isEqualTo("UPDATED");
    }

    @Test
    void testUpdateCustomerNotExists() {
        assertThrows(NotFoundException.class,
                () -> customerController.updateCustomer(UUID.randomUUID(), null));
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerExists() {
        Customer customer = customerRepository.findAll().get(0);

        customerController.deleteCustomerBy(customer.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    void testDeleteCustomerNotExists() {
        assertThrows(NotFoundException.class,
                () -> customerController.deleteCustomerBy(UUID.randomUUID()));
    }
}

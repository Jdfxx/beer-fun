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
    void getCustomerByIdExist() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertThat(customerDTO).isNotNull();
    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(NotFoundException.class,
                ()-> customerController.getCustomerById(UUID.randomUUID()));
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
        Customer savedBeer = customerRepository.findById(customerId).get();
        assertThat(savedBeer).isNotNull();
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
    void testUpdateBeerNotExists() {
        assertThrows(NotFoundException.class,
                () -> customerController.updateCustomer(UUID.randomUUID(), null));
    }
}

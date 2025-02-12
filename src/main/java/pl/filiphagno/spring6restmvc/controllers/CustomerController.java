package pl.filiphagno.spring6restmvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filiphagno.spring6restmvc.model.Customer;
import pl.filiphagno.spring6restmvc.services.CustomerService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1")
@RestController()
public class CustomerController {

    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.addCustomer(customer);
        return ResponseEntity.created(URI.create("/api/v1/customer/" + newCustomer.id()))
                .build();
    }

    @RequestMapping("/customers")
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/customer/{id}")
    public Customer getCustomersById(@PathVariable("id") UUID id) {
        return customerService.getCustomersById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") UUID id, @RequestBody Customer customer) {
        customerService.getCustomersById(id).orElseThrow(NotFoundException::new);
        customerService.updateCustomer(id, customer);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("customer/{id}")
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable("id") UUID id) {
        Customer removedCustomer = customerService.deleteCustomerById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(removedCustomer);
    }
}

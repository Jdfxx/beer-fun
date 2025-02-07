package pl.filiphagno.spring6restmvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.filiphagno.spring6restmvc.model.Customer;
import pl.filiphagno.spring6restmvc.services.CustomerService;

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

    @RequestMapping("/customers")
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/customer/{id}")
    public Customer getCustomersById(@PathVariable("id") UUID id) {
        return customerService.getCustomersById(id);
    }
}

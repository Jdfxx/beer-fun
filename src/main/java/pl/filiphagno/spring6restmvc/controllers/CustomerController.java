package pl.filiphagno.spring6restmvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;
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
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO newCustomerDTO = customerService.addCustomer(customerDTO);
        return ResponseEntity.created(URI.create("/api/v1/customer/" + newCustomerDTO.id()))
                .build();
    }

    @RequestMapping("/customers")
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/customer/{id}")
    public CustomerDTO getCustomersById(@PathVariable("id") UUID id) {
        return customerService.getCustomersById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody CustomerDTO customerDTO) {
        customerService.getCustomersById(id).orElseThrow(NotFoundException::new);
        customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("customer/{id}")
    public ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable("id") UUID id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok().build();
    }
}

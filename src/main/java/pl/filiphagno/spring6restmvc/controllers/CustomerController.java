package pl.filiphagno.spring6restmvc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<CustomerDTO> addCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        CustomerDTO newCustomerDTO = customerService.addCustomer(customerDTO);
        return ResponseEntity.created(URI.create("/api/v1/customer/" + newCustomerDTO.id()))
                .build();
    }

    @RequestMapping("/customers")
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(value = "/customer/{id}")
    public CustomerDTO getCustomerBy(@PathVariable("id") UUID id) {
        return customerService.getCustomersById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/customer/{id}")
    public CustomerDTO updateCustomer(@PathVariable("id") UUID id, @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("customer/{id}")
    public CustomerDTO deleteCustomerBy(@PathVariable("id") UUID id) {
       return customerService.deleteCustomerById(id).orElseThrow(NotFoundException::new);

    }
}

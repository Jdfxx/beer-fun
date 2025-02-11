package pl.filiphagno.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.filiphagno.spring6restmvc.model.Customer;
import pl.filiphagno.spring6restmvc.services.CustomerService;
import pl.filiphagno.spring6restmvc.services.CustomerServiceImpl;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    static CustomerServiceImpl customerServiceImpl;

    static Customer testCustomer;


    @BeforeAll
    static void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
        testCustomer = customerServiceImpl.listCustomers().getFirst();
    }

    @Test
    void createCustomer() throws Exception {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("Test Name")
                .build();
        given(customerService.addCustomer(any(Customer.class))).willReturn(customer);

        mockMvc.perform(post("/api/v1/customer")
            .content(objectMapper.writeValueAsString(customer))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "/api/v1/customer/" + customer.id()));

    }

    @Test
    void getListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get("/api/v1/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {
        given(customerService.getCustomersById(any(UUID.class))).willReturn(testCustomer);

        mockMvc.perform(get("/api/v1/customer/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.id().toString())))
                .andExpect(jsonPath("$.name", is(testCustomer.name())));
    }

    @Test
    void updateCustomer() throws Exception {
        given(customerService.getCustomersById(any(UUID.class))).willReturn(testCustomer);

        mockMvc.perform(put("/api/v1/customer/" + testCustomer.id())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer))
                .contentType(MediaType.APPLICATION_JSON));

        verify(customerService).updateCustomer(any(UUID.class), any(Customer.class));
    }

    @Test
    void deleteCustomer() throws Exception {
        given(customerService.deleteCustomerById(any(UUID.class))).willReturn(testCustomer);

        mockMvc.perform(delete("/api/v1/customer/" + testCustomer.id())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.id().toString())));

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(argumentCaptor.capture());
        assertThat(testCustomer.id()).isEqualTo(argumentCaptor.getValue());
    }
}
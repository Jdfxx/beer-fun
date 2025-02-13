package pl.filiphagno.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;
import pl.filiphagno.spring6restmvc.services.CustomerService;

import java.util.Optional;
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

    @Autowired
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void createCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Test Name")
                .build();
        given(customerService.addCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

        mockMvc.perform(post("/api/v1/customer")
            .content(objectMapper.writeValueAsString(customerDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "/api/v1/customer/" + customerDTO.id()));

    }

    @Test
    void getListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerService.listCustomers());

        mockMvc.perform(get("/api/v1/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();

        given(customerService.getCustomersById(any(UUID.class))).willReturn(Optional.ofNullable(testCustomerDTO));

        mockMvc.perform(get("/api/v1/customer/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomerDTO.id().toString())))
                .andExpect(jsonPath("$.name", is(testCustomerDTO.name())));
    }

    @Test
    void updateCustomer() throws Exception {

        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();
        given(customerService.getCustomersById(any(UUID.class))).willReturn(Optional.ofNullable(testCustomerDTO));

        mockMvc.perform(put("/api/v1/customer/" + testCustomerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO))
                .contentType(MediaType.APPLICATION_JSON));

        verify(customerService).updateCustomer(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();
        mockMvc.perform(delete("/api/v1/customer/" + testCustomerDTO.id())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomerDTO.id().toString())));

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(argumentCaptor.capture());
        assertThat(testCustomerDTO.id()).isEqualTo(argumentCaptor.getValue());
    }
}
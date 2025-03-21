package pl.filiphagno.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.filiphagno.spring6restmvc.model.CustomerDTO;
import pl.filiphagno.spring6restmvc.security.SpringSecurityConfig;
import pl.filiphagno.spring6restmvc.services.CustomerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.controllers.BeerControllerTest.*;

@WebMvcTest(CustomerController.class)
@Import(SpringSecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    List<CustomerDTO> customerDTOList;

    @BeforeEach
    void setUp() {
        customerDTOList = setupData();
    }

    private List<CustomerDTO> setupData() {

        List<CustomerDTO> customerDTOList = new ArrayList<>();
        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Frank Doe")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        customerDTOList.add(customer1);
        customerDTOList.add(customer2);
        customerDTOList.add(customer3);

        return customerDTOList;
    }

    @Test
    void createCustomer() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Test Name")
                .build();
        given(customerService.addCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

        mockMvc.perform(post("/api/v1/customer")
            .content(objectMapper.writeValueAsString(customerDTO))
                        .with(getJwt())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "/api/v1/customer/" + customerDTO.id()));

    }

    @Test
    void createCustomerBadRequest() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(null)
                .build();
        given(customerService.addCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

        mockMvc.perform(post("/api/v1/customer")
                        .content(objectMapper.writeValueAsString(customerDTO))
                        .with(getJwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));

    }

    @Test
    void getListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerDTOList);

        mockMvc.perform(get("/api/v1/customers").accept(MediaType.APPLICATION_JSON)
                        .with(getJwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerBy() throws Exception {
        given(customerService.listCustomers()).willReturn(customerDTOList);
        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();

        given(customerService.getCustomersById(any(UUID.class))).willReturn(Optional.ofNullable(testCustomerDTO));

        mockMvc.perform(get("/api/v1/customer/" + UUID.randomUUID())
                        .with(getJwt())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomerDTO.id().toString())))
                .andExpect(jsonPath("$.name", is(testCustomerDTO.name())));
    }

    @Test
    void updateCustomer() throws Exception {
        given(customerService.listCustomers()).willReturn(customerDTOList);
        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();
        given(customerService.getCustomersById(any(UUID.class))).willReturn(Optional.ofNullable(testCustomerDTO));

        mockMvc.perform(put("/api/v1/customer/" + testCustomerDTO.id())
                .with(getJwt())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO))
                .contentType(MediaType.APPLICATION_JSON));

        verify(customerService).updateCustomer(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void updateCustomerBadRequest() throws Exception {
        given(customerService.listCustomers()).willReturn(customerDTOList);
        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();
        UUID id = testCustomerDTO.id();
        testCustomerDTO = new CustomerDTO(id, null, null, null,null, null);
        given(customerService.getCustomersById(any(UUID.class))).willReturn(Optional.ofNullable(testCustomerDTO));

        mockMvc.perform(put("/api/v1/customer/" + testCustomerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                        .with(getJwt())
                .content(objectMapper.writeValueAsString(testCustomerDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));

    }

    @Test
    void deleteCustomer() throws Exception {
        given(customerService.listCustomers()).willReturn(customerDTOList);
        CustomerDTO testCustomerDTO = customerService.listCustomers().getFirst();
        mockMvc.perform(delete("/api/v1/customer/" + testCustomerDTO.id())
                .with(getJwt()));
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(argumentCaptor.capture());
        assertThat(testCustomerDTO.id()).isEqualTo(argumentCaptor.getValue());
    }
}
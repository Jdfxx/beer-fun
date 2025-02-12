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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.services.BeerService;
import pl.filiphagno.spring6restmvc.services.BeerServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.controllers.BeerController.BASE_URI;
import static pl.filiphagno.spring6restmvc.model.BeerStyle.STOUT;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    static BeerDTO testBeerDTO;
    static BeerServiceImpl beerServiceImpl;

    @BeforeAll
    static void setUp() {
        beerServiceImpl = new BeerServiceImpl();
        testBeerDTO = beerServiceImpl.listBeers().getFirst();
    }

    @Test
    void createBeer() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerStyle(STOUT)
                .quantityOnHand(200)
                .price(BigDecimal.valueOf(12.5))
                .build();

        given(beerService.addBeer(any(BeerDTO.class))).willReturn(beerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI +"/beer")
                    .content(objectMapper.writeValueAsString(beerDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", BASE_URI + "/beer/" + beerDTO.id()));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beer/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception {
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI +"/beer/" + testBeerDTO.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeerDTO.id().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeerDTO.beerName())));
    }

    @Test
    void getListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void updateBeerWhenBeerExist() throws Exception {
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URI + "/beer/" + testBeerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeerDTO))
                .contentType(MediaType.APPLICATION_JSON));

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void deleteBeer() throws Exception {
        given(beerService.removeBeerById(testBeerDTO.id())).willReturn(Optional.ofNullable(testBeerDTO));
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/beer/" + testBeerDTO.id())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).removeBeerById(argumentCaptor.capture());
        assertThat(testBeerDTO.id()).isEqualTo(argumentCaptor.getValue());
    }
}
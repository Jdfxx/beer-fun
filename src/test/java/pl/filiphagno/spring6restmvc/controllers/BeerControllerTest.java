package pl.filiphagno.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.filiphagno.spring6restmvc.model.Beer;
import pl.filiphagno.spring6restmvc.services.BeerService;
import pl.filiphagno.spring6restmvc.services.BeerServiceImpl;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.model.BeerStyle.STOUT;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;


    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void createBeer() throws Exception {
        Beer beer = Beer.builder()
                .id(UUID.randomUUID())
                .beerStyle(STOUT)
                .quantityOnHand(200)
                .price(BigDecimal.valueOf(12.5))
                .build();

        given(beerService.addBeer(any(Beer.class))).willReturn(beer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/beer")
                    .content(objectMapper.writeValueAsString(beer))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "api/v1/beer/" + beer.id()));
    }

    @Test
    void getBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getFirst();

        given(beerService.getBeerById(testBeer.id())).willReturn(testBeer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + testBeer.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.id().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.beerName())));
    }

    @Test
    void getListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
}
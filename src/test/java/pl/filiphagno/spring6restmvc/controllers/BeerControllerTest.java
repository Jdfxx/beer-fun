package pl.filiphagno.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.services.BeerService;

import java.math.BigDecimal;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.controllers.BeerController.BASE_URI;
import static pl.filiphagno.spring6restmvc.model.BeerStyle.STOUT;

@WebMvcTest(controllers = BeerController.class)
class BeerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    List<BeerDTO> beerDTOList;

    @BeforeEach
    void setUp() {
        beerDTOList = setupData();
    }

    private List<BeerDTO> setupData() {
        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("SomeBeer1")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .upc("test")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();


        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("SomeBeer2")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .upc("test")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();


        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("SomeBeer3")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .upc("test")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        List<BeerDTO> beerDTOList = new ArrayList<>();
        beerDTOList.add(beer1);
        beerDTOList.add(beer2);
        beerDTOList.add(beer3);
        return beerDTOList;
    }

    @Test
    void createBeerNoNme() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerStyle(STOUT)
                .beerName(null)
                .quantityOnHand(200)
                .build();

        given(beerService.addBeer(any(BeerDTO.class))).willReturn(beerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI +"/beer")
                        .content(objectMapper.writeValueAsString(beerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void createBeer() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerStyle(STOUT)
                .upc("test")
                .beerName("Test name")
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
    void getBeerByNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beer/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerBy() throws Exception {
        given(beerService.listBeers(null, null)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null).stream().findFirst().orElse(null);
        assert testBeerDTO != null;
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
        given(beerService.listBeers(null, null)).willReturn(beerDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void updateBeerWhenBeerExist() throws Exception {
        given(beerService.listBeers(null, null)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null).stream().findFirst().orElse(null);
        assert testBeerDTO != null;
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URI + "/beer/" + testBeerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeerDTO))
                .contentType(MediaType.APPLICATION_JSON));

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void updateBeerWhenBeerBadRequest() throws Exception {
        given(beerService.listBeers(null, null)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null).stream().findFirst().orElse(null);
        UUID id = testBeerDTO.id();
        testBeerDTO = new BeerDTO(id, null, null,
                null, null, null, null, null, null);
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URI + "/beer/" + testBeerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeerDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)));

    }


    @Test
    void deleteBeerById() throws Exception {
        given(beerService.listBeers(null, null)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null).stream().findFirst().orElse(null);
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/beer/" + testBeerDTO.id()));
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).removeBeerById(argumentCaptor.capture());
        assertThat(testBeerDTO.id()).isEqualTo(argumentCaptor.getValue());
    }

}
package pl.filiphagno.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.security.SpringSecurityConfig;
import pl.filiphagno.spring6restmvc.services.BeerService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.controllers.BeerController.BASE_URI;
import static pl.filiphagno.spring6restmvc.model.BeerStyle.STOUT;

@WebMvcTest(controllers = BeerController.class)
@Import(SpringSecurityConfig.class)
public class BeerControllerTest {

    public static final String USERNAME = "user1";
    public static final String PASSWORD = "password";
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    Page<BeerDTO> beerDTOList;

    @BeforeEach
    void setUp() {
        beerDTOList = setupData();
    }

    private Page<BeerDTO> setupData() {
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
        return new PageImpl<>(Arrays.asList(beer1, beer2, beer3));
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
                        .with(getJwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(3)));
    }

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getJwt() {
        return jwt().jwt(jwt -> {
            jwt.claims(claims -> {
                        claims.put("scope", "message-read");
                        claims.put("scope", "message-write");
                    })
                    .subject("messaging-client")
                    .notBefore(Instant.now().minusSeconds(5l));
        });
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
                    .with(getJwt())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", BASE_URI + "/beer/" + beerDTO.id()));
    }

    @Test
    void getBeerByNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beer/" + UUID.randomUUID())
                        .with(getJwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerBy() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any(), any())).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        assert testBeerDTO != null;
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI +"/beer/" + testBeerDTO.id())
                        .with(getJwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeerDTO.id().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeerDTO.beerName())));
    }

    @Test
    void getListBeers() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any(), any())).willReturn(beerDTOList);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beers")
                        .with(getJwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size", is(3)));
    }

    @Test
    void updateBeerWhenBeerExist() throws Exception {
        given(beerService.listBeers(null, null, false, 1, 25)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        assert testBeerDTO != null;
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URI + "/beer/" + testBeerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                .with(getJwt())
                .content(objectMapper.writeValueAsString(testBeerDTO))
                .contentType(MediaType.APPLICATION_JSON));

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void updateBeerWhenBeerBadRequest() throws Exception {
        given(beerService.listBeers(null, null, false, 1, 25)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        UUID id = testBeerDTO.id();
        testBeerDTO = new BeerDTO(id, null, null,
                null, null, null, null, null, null);
        given(beerService.getBeerById(testBeerDTO.id())).willReturn(Optional.of(testBeerDTO));
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URI + "/beer/" + testBeerDTO.id())
                .accept(MediaType.APPLICATION_JSON)
                        .with(getJwt())
                .content(objectMapper.writeValueAsString(testBeerDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)));

    }


    @Test
    void deleteBeerById() throws Exception {
        given(beerService.listBeers(null, null, false, 1, 25)).willReturn(beerDTOList);
        BeerDTO testBeerDTO = beerService.listBeers(null, null, false, 1, 25).stream().findFirst().orElse(null);
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URI + "/beer/" + testBeerDTO.id())
                .with(getJwt()));
        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).removeBeerById(argumentCaptor.capture());
        assertThat(testBeerDTO.id()).isEqualTo(argumentCaptor.getValue());
    }

}
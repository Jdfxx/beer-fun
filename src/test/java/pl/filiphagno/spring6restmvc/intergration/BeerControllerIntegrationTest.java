package pl.filiphagno.spring6restmvc.intergration;

import jakarta.transaction.Transactional;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.filiphagno.spring6restmvc.controllers.BeerController;
import pl.filiphagno.spring6restmvc.controllers.BeerControllerTest;
import pl.filiphagno.spring6restmvc.controllers.NotFoundException;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.controllers.BeerController.BASE_URI;
import static pl.filiphagno.spring6restmvc.controllers.BeerControllerTest.getJwt;

@SpringBootTest
public class BeerControllerIntegrationTest {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testListBeers() {
        Page<BeerDTO> beers = beerController.listBeers(null, null, null, 1, 1000);
        assertThat(beers.getSize()).isEqualTo(1000);
    }

    @Rollback(true)
    @Transactional
    @Test
    void testEmptyListBeers() {
        beerRepository.deleteAll();
        Page<BeerDTO> beers = beerController.listBeers(null, null, null, null, null);
        assertThat(beers.stream().count()).isEqualTo(0);
    }

    @Test
    void testGetBeerBy() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerController.getBeerBy(beer.getId());

        assertThat(beerDTO).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class,
                () -> beerController.getBeerBy(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void testAddBeer() throws URISyntaxException {
        BeerDTO beer = BeerDTO.builder()
                .beerName("testBeer")
                .build();
        ResponseEntity<String> responseEntity = beerController.createBeer(beer);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID beerId = UUID.fromString(location[4]);
        Beer savedBeer = beerRepository.findById(beerId).get();
        assertThat(savedBeer).isNotNull();
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateBeerExists()  {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = BeerDTO.builder()
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(beer.getCreatedDate())
                .beerName("UPDATED")
                .build();
        beerController.updateBeer(beer.getId(), beerDTO);

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer).isNotNull();
        assertThat(updatedBeer.getBeerStyle()).isEqualTo(beer.getBeerStyle());
        assertThat(updatedBeer.getQuantityOnHand()).isEqualTo(beer.getQuantityOnHand());
        assertThat(updatedBeer.getCreatedDate()).isEqualTo(beer.getCreatedDate());
        assertThat(updatedBeer.getUpc()).isEqualTo(beer.getUpc());
        assertThat(updatedBeer.getBeerName()).isEqualTo("UPDATED");
    }

    @Test
    void testUpdateBeerNotExists() {
        assertThrows(NotFoundException.class,
                () -> beerController.updateBeer(UUID.randomUUID(), null));
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerExists() {
        Beer Beer = beerRepository.findAll().getFirst();

        beerController.deleteBeerBy(Beer.getId());

        Optional<Beer> deletedCustomer = beerRepository.findById(Beer.getId());
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    void testDeleteCustomerNotExists() {
        assertThrows(NotFoundException.class,
                () -> beerController.deleteBeerBy(UUID.randomUUID()));
    }

    @Test
    void listBeersByName() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .with(getJwt())
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "500"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(336)));
    }

    @Test
    void listBeersByStyle() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .with(getJwt())
                        .queryParam("beerStyle", BeerStyle.PORTER.name())
                        .queryParam("pageSize", "500"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(71)));
    }

    @Test
    void listBeersByNameAndStyle() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .with(getJwt())
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "500"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.size()", is(310)));
    }

    @Test
    void listBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "500")
                        .with(getJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void listBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .with(getJwt())
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void listBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .with(getJwt())
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(50)));
    }

    @Test
    void authFailsWithWrongPassword() throws Exception {
        mockMvc.perform(get(BASE_URI + "/beers")
                        .with(httpBasic("some", "some")))
                .andExpect(status().isUnauthorized());
    }

}

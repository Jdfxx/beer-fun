package pl.filiphagno.spring6restmvc.intergration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.filiphagno.spring6restmvc.controllers.BeerController;
import pl.filiphagno.spring6restmvc.controllers.NotFoundException;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.filiphagno.spring6restmvc.controllers.BeerController.BASE_URI;

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testListBeers() {
        List<BeerDTO> beers = beerController.listBeers(null, null);
        assertThat(beers.size()).isEqualTo(2413);
    }

    @Transactional
    @Test
    void testEmptyListBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> beers = beerController.listBeers(null, null);
        assertThat(beers.size()).isEqualTo(0);
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
        Beer Beer = beerRepository.findAll().get(0);

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
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beers")
                        .queryParam("beerName", "IPA"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @Test
    void listBeersByStyle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/beers")
                        .queryParam("beerStyle", BeerStyle.PORTER.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(71)));
    }
}

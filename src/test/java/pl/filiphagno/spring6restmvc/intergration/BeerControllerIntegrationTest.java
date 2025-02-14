package pl.filiphagno.spring6restmvc.intergration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import pl.filiphagno.spring6restmvc.controllers.BeerController;
import pl.filiphagno.spring6restmvc.controllers.NotFoundException;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BeerControllerIntegrationTest {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers() {
        List<BeerDTO> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Transactional
    @Test
    void testEmptyListBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(0);
    }

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());

        assertThat(beerDTO).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class,
                () -> beerController.getBeerById(UUID.randomUUID()));
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
}

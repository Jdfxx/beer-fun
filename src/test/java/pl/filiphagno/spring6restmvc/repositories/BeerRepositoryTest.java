package pl.filiphagno.spring6restmvc.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.filiphagno.spring6restmvc.bootstrap.BoostrapData;
import pl.filiphagno.spring6restmvc.entities.Beer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    BoostrapData boostrapData;

    @BeforeEach
    void setUp() {
        boostrapData = new BoostrapData(beerRepository, customerRepository);
    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Test name")
                .build());

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Test name");
        assertThat(savedBeer.getId()).isNotNull();
    }
}
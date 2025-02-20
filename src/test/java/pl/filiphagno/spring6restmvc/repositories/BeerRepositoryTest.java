package pl.filiphagno.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.filiphagno.spring6restmvc.bootstrap.BoostrapData;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.services.BeerCsvService;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    BoostrapData boostrapData;

    @MockitoBean
    private BeerCsvService beerCsvService;

    @BeforeEach
    void setUp() {
        boostrapData = new BoostrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Test name")
                .beerStyle(BeerStyle.PORTER)
                .upc("Test upc")
                .price(BigDecimal.valueOf(10.8))
                .build());
        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Test name");
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameToLong() {
        assertThrows(ConstraintViolationException.class, ()-> {
            beerRepository.save(Beer.builder()
                    .beerName("Test nameasdasdcasdcascdacsdcasdcascdascdcasdcasdcascdcasdcascdcascdascdcascdacsdcascd")
                    .beerStyle(BeerStyle.PORTER)
                    .upc("Test upc")
                    .price(BigDecimal.valueOf(10.8))
                    .build());
            beerRepository.flush();
                });
    }
}
package pl.filiphagno.spring6restmvc.bootstrap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.entities.Customer;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;
import pl.filiphagno.spring6restmvc.repositories.CustomerRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BoostrapDataTest {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    BoostrapData boostrapData;

    @BeforeEach
    void setUp() {
        boostrapData = new BoostrapData(beerRepository, customerRepository);
    }

    @Test
    void testFindAllBeers() throws Exception {
        boostrapData.run();
        List<Beer> beers = beerRepository.findAll();
        assertThat(beers).isNotNull();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Test
    void testFindAllCustomers() throws Exception {
        boostrapData.run();
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(3);
    }
}
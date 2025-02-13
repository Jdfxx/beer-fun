package pl.filiphagno.spring6restmvc.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.entities.Customer;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;
import pl.filiphagno.spring6restmvc.repositories.CustomerRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BoostrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("SomeBeer1")
                    .beerStyle(BeerStyle.PORTER)
                    .price(new BigDecimal("4.65"))
                    .quantityOnHand(12)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();


            Beer beer2 = Beer.builder()
                    .beerName("SomeBeer2")
                    .beerStyle(BeerStyle.PORTER)
                    .price(new BigDecimal("4.65"))
                    .quantityOnHand(12)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();


            Beer beer3 = Beer.builder()
                    .beerName("SomeBeer3")
                    .beerStyle(BeerStyle.PORTER)
                    .price(new BigDecimal("4.65"))
                    .quantityOnHand(12)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();
            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }

    }
    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .name("John Doe")
                    .created(LocalDateTime.now())
                    .updated(LocalDateTime.now())
                    .build();
            Customer customer2 = Customer.builder()
                    .name("Jane Doe")
                    .created(LocalDateTime.now())
                    .updated(LocalDateTime.now())
                    .build();
            Customer customer3 = Customer.builder()
                    .name("Frank Doe")
                    .created(LocalDateTime.now())
                    .updated(LocalDateTime.now())
                    .build();

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);
        }
    }
}

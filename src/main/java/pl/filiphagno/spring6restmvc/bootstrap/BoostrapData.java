package pl.filiphagno.spring6restmvc.bootstrap;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.entities.Customer;
import pl.filiphagno.spring6restmvc.model.BeerCSVRecord;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;
import pl.filiphagno.spring6restmvc.repositories.CustomerRepository;
import pl.filiphagno.spring6restmvc.services.BeerCsvService;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoostrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
        LoadCsvData();
    }

    private void LoadCsvData() throws FileNotFoundException {
        if(beerRepository.count() < 10) {
            File csvFile = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> records = beerCsvService.convertCsv(csvFile);

            records.forEach((beerCSVRecord) -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerStyle(beerStyle)
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("SomeBeer1")
                    .beerStyle(BeerStyle.PORTER)
                    .price(new BigDecimal("4.65"))
                    .quantityOnHand(12)
                    .upc("123456789")
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();


            Beer beer2 = Beer.builder()
                    .beerName("SomeBeer2")
                    .beerStyle(BeerStyle.PORTER)
                    .price(new BigDecimal("4.65"))
                    .quantityOnHand(12)
                    .upc("123456789")
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();


            Beer beer3 = Beer.builder()
                    .beerName("SomeBeer3")
                    .beerStyle(BeerStyle.PORTER)
                    .price(new BigDecimal("4.65"))
                    .quantityOnHand(12)
                    .upc("123456789")
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

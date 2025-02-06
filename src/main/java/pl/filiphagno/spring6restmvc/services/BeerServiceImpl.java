package pl.filiphagno.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.model.Beer;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {

        log.debug("Service: getBeerById: {}", id);
        return Beer.builder()
                .id(id)
                .version(1)
                .beerName("SomeBeer")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }
}

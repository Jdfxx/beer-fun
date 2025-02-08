package pl.filiphagno.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.model.Beer;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("SomeBeer1")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();


        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("SomeBeer2")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();


        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("SomeBeer3")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        beerMap.put(beer1.id(), beer1);
        beerMap.put(beer2.id(), beer2);
        beerMap.put(beer3.id(), beer3);
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer addBeer(Beer beer) {
        Beer newBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .upc(beer.upc())
                .beerName(beer.beerName())
                .beerStyle(beer.beerStyle())
                .price(beer.price())
                .quantityOnHand(beer.quantityOnHand())
                .build();

        beerMap.put(newBeer.id(), newBeer);
        return newBeer;
    }

    @Override
    public void updateBeer(UUID id, Beer beer) {
        Beer beerToUpdate = beerMap.get(id);
        Beer updatedBeer = Beer.builder()
                .id(beerToUpdate.id())
                .createdDate(beerToUpdate.createdDate())
                .updatedDate(LocalDateTime.now())
                .upc(beer.upc())
                .beerName(beer.beerName())
                .beerStyle(beer.beerStyle())
                .price(beer.price())
                .quantityOnHand(beer.quantityOnHand())
                .version(beerToUpdate.version() + 1)
                .build();
        beerMap.put(updatedBeer.id(), updatedBeer);
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Service: getBeerById: {}", id);
        return beerMap.get(id);
    }

    @Override
    public Beer removeBeerById(UUID id) {
        return beerMap.remove(id);
    }
}

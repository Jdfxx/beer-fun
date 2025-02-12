package pl.filiphagno.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beerDTO1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("SomeBeer1")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();


        BeerDTO beerDTO2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("SomeBeer2")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();


        BeerDTO beerDTO3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("SomeBeer3")
                .beerStyle(BeerStyle.PORTER)
                .price(new BigDecimal("4.65"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        beerMap.put(beerDTO1.id(), beerDTO1);
        beerMap.put(beerDTO2.id(), beerDTO2);
        beerMap.put(beerDTO3.id(), beerDTO3);
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public BeerDTO addBeer(BeerDTO beerDTO) {
        BeerDTO newBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .upc(beerDTO.upc())
                .beerName(beerDTO.beerName())
                .beerStyle(beerDTO.beerStyle())
                .price(beerDTO.price())
                .quantityOnHand(beerDTO.quantityOnHand())
                .build();

        beerMap.put(newBeerDTO.id(), newBeerDTO);
        return newBeerDTO;
    }

    @Override
    public void updateBeer(UUID id, BeerDTO beerDTO) {
        BeerDTO beerDTOToUpdate = beerMap.get(id);
        BeerDTO updatedBeerDTO = BeerDTO.builder()
                .id(beerDTOToUpdate.id())
                .createdDate(beerDTOToUpdate.createdDate())
                .updatedDate(LocalDateTime.now())
                .upc(beerDTO.upc())
                .beerName(beerDTO.beerName())
                .beerStyle(beerDTO.beerStyle())
                .price(beerDTO.price())
                .quantityOnHand(beerDTO.quantityOnHand())
                .version(beerDTOToUpdate.version() + 1)
                .build();
        beerMap.put(updatedBeerDTO.id(), updatedBeerDTO);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Service: getBeerById: {}", id);
        return Optional.ofNullable(beerMap.get(id));
    }

    @Override
    public Optional<BeerDTO> removeBeerById(UUID id) {
        return Optional.ofNullable(beerMap.remove(id));
    }
}

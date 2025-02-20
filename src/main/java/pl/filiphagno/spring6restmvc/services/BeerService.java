package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    BeerDTO addBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beerDTO);

    Optional<BeerDTO> removeBeerById(UUID id);
}

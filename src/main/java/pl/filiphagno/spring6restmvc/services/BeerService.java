package pl.filiphagno.spring6restmvc.services;

import org.springframework.data.domain.Page;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<BeerDTO> getBeerById(UUID id);

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageLimit);

    BeerDTO addBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beerDTO);

    Optional<BeerDTO> removeBeerById(UUID id);
}

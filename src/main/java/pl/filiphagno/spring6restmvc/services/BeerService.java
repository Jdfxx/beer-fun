package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<Beer> getBeerById(UUID id);

    List<Beer> listBeers();

    Beer addBeer(Beer beer);

    void updateBeer(UUID id, Beer beer);

    Optional<Beer> removeBeerById(UUID id);
}

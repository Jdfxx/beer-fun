package pl.filiphagno.spring6restmvc.services;

import pl.filiphagno.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);

    List<Beer> listBeers();

}

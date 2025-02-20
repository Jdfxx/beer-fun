package pl.filiphagno.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    List<Beer>  findAllByBeerNameIsLikeIgnoreCase(String beerName);
    List<Beer>  findAllByBeerStyle(BeerStyle beerStyle);
    List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);
}

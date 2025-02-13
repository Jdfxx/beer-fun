package pl.filiphagno.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.model.BeerDTO;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDto);
    BeerDTO beerToBeerDTO(Beer beer);
}

package pl.filiphagno.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.mappers.BeerMapper;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    BeerRepository beerRepository;
    BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll().stream()
                .map(beer -> beerMapper.beerToBeerDTO(beer))
                .collect(Collectors.toList());
    }

    @Override
    public BeerDTO addBeer(BeerDTO beerDTO) {
        Beer savedBeer = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO));
        return beerMapper.beerToBeerDTO(savedBeer);
    }

    @Override
    public void updateBeer(UUID id, BeerDTO beerDTO) {

    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("Service: getBeerById: {}", id);
        if(beerRepository.findById(id).isPresent()) {
            return Optional.of(beerMapper.beerToBeerDTO(beerRepository.findById(id).get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void removeBeerById(UUID id) {
        beerRepository.deleteById(id);
    }
}

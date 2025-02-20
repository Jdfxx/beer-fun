package pl.filiphagno.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.filiphagno.spring6restmvc.entities.Beer;
import pl.filiphagno.spring6restmvc.mappers.BeerMapper;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.repositories.BeerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        List<Beer> beerList;

        if (StringUtils.hasText(beerName)) {
            beerList = listBeersByName(beerName);
        } else if (beerStyle != null) {
            beerList = listBeersByStyle(beerStyle);
        }else {
            beerList = beerRepository.findAll();
        }

        return beerList.stream().map(beerMapper::beerToBeerDTO).collect(Collectors.toList());
    }

    private List<Beer> listBeersByStyle(BeerStyle beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle);
    }

    private List<Beer> listBeersByName(String beerName) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public BeerDTO addBeer(BeerDTO beerDTO) {
        Beer savedBeer = beerRepository.save(beerMapper.beerDtoToBeer(beerDTO));
        return beerMapper.beerToBeerDTO(savedBeer);
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(beer -> {
            beer.setBeerName(beerDTO.beerName());
            beer.setBeerStyle(beerDTO.beerStyle());
            beer.setUpc(beerDTO.upc());
            beer.setPrice(beerDTO.price());
            atomicReference.set(Optional.of(beerMapper.beerToBeerDTO(beerRepository.save(beer))));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDTO(
                beerRepository.findById(id).orElse(null)));
    }

    @Override
    public Optional<BeerDTO> removeBeerById(UUID id) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(
                existingBeer -> {
                    beerRepository.delete(existingBeer);
                    atomicReference.set(Optional.of(beerMapper.beerToBeerDTO(existingBeer)));
                },
                () -> atomicReference.set(Optional.empty())
        );
        return atomicReference.get();
    }
}

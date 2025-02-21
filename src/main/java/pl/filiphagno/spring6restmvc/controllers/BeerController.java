package pl.filiphagno.spring6restmvc.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.model.BeerStyle;
import pl.filiphagno.spring6restmvc.services.BeerService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static pl.filiphagno.spring6restmvc.controllers.BeerController.BASE_URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_URI)
public class BeerController {
    public static final String BASE_URI = "/api/v1";
    private final BeerService beerService;

    @RequestMapping("/beers")
    public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false) BeerStyle beerStyle,
                                   @RequestParam(required = false) Boolean showInventory,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {

        return beerService.listBeers(beerName, beerStyle, showInventory,
                pageNumber, pageSize);
    }

    @GetMapping(value = "/beer/{beerId}")
    public BeerDTO getBeerBy(@PathVariable("beerId") UUID beerId) {
        log.debug("Controller: Got beer by id {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = "/beer")
    public ResponseEntity<String> createBeer(@Validated @RequestBody BeerDTO beerDTO) throws URISyntaxException {
        BeerDTO savedBeerDTO = beerService.addBeer(beerDTO);
        return ResponseEntity.created(new URI(BASE_URI +"/beer/" + savedBeerDTO.id())).build();
    }

    @PutMapping("/beer/{id}")
    public BeerDTO updateBeer(@PathVariable("id") UUID id, @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(id, beerDTO).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("beer/{id}")
    public BeerDTO deleteBeerBy(@PathVariable("id") UUID id) {
        return beerService.removeBeerById(id).orElseThrow(NotFoundException::new);
    }
}

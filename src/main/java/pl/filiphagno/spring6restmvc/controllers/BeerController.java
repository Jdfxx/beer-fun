package pl.filiphagno.spring6restmvc.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filiphagno.spring6restmvc.model.BeerDTO;
import pl.filiphagno.spring6restmvc.services.BeerService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
    public List<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(value = "/beer/{beerId}")
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Controller: Got beer by id {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = "/beer")
    public ResponseEntity<String> createBeer(@RequestBody BeerDTO beerDTO) throws URISyntaxException {
        BeerDTO savedBeerDTO = beerService.addBeer(beerDTO);
        return ResponseEntity.created(new URI(BASE_URI +"/beer/" + savedBeerDTO.id())).build();
    }

    @PutMapping("/beer/{id}")
    public BeerDTO updateBeer(@PathVariable("id") UUID id, @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(id, beerDTO).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("beer/{id}")
    public ResponseEntity<String> deleteBeer(@PathVariable("id") UUID id) {
        beerService.removeBeerById(id);
        return ResponseEntity.ok().build();
    }
}

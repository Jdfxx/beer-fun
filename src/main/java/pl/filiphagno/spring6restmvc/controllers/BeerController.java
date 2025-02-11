package pl.filiphagno.spring6restmvc.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filiphagno.spring6restmvc.model.Beer;
import pl.filiphagno.spring6restmvc.services.BeerService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BeerController {
    private final BeerService beerService;

    @RequestMapping("/beers")
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping(value = "/beer/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Controller: Got beer by id {}", beerId);
        return beerService.getBeerById(beerId);
    }

    @PostMapping(value = "/beer")
    public ResponseEntity<String> createBeer(@RequestBody Beer beer) throws URISyntaxException {
        Beer savedBeer = beerService.addBeer(beer);
        return ResponseEntity.created(new URI("api/v1/beer/" + savedBeer.id())).build();
    }

    @PutMapping("/beer/{id}")
    public ResponseEntity<String> updateBeer(@PathVariable("id") UUID id, @RequestBody Beer beer) {
        log.debug("Controller: Got beer by id {} to be updated", id);
        Beer beerToUpdate = beerService.getBeerById(id);
        if (beerToUpdate == null) {
            return ResponseEntity.notFound().build();
        } else {
            beerService.updateBeer(id, beer);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("beer/{id}")
    public ResponseEntity<Beer> deleteBeer(@PathVariable("id") UUID id) {
        Beer removedBeer =  beerService.removeBeerById(id);
        if(removedBeer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(removedBeer);
        }
    }

}

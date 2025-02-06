package pl.filiphagno.spring6restmvc.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import pl.filiphagno.spring6restmvc.model.Beer;
import pl.filiphagno.spring6restmvc.services.BeerService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        log.debug("Controller: Get beer by id {}", id);
        return beerService.getBeerById(id);
    }
}

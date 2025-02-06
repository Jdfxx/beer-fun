package pl.filiphagno.spring6restmvc.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.filiphagno.spring6restmvc.model.Beer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerTest {

    @Autowired
    BeerController controller;



    @Test
    void getBeerByIdTest() {
        UUID id = UUID.randomUUID();
        Beer beer = controller.getBeerById(id);
        assertEquals(id, beer.getId());
    }
}
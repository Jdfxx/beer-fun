package pl.filiphagno.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filiphagno.spring6restmvc.entities.BeerOrder;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {
}

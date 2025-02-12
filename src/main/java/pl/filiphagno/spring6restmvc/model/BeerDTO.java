package pl.filiphagno.spring6restmvc.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BeerDTO(
    UUID id,
    Integer version,
    String beerName,
    BeerStyle beerStyle,
    String upc,
    Integer quantityOnHand,
    BigDecimal price,
    LocalDateTime createdDate,
    LocalDateTime updatedDate) {
}

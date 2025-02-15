package pl.filiphagno.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BeerDTO(
    UUID id,
    Integer version,
    @NotBlank String beerName,
    @NotNull BeerStyle beerStyle,
    @NotBlank String upc,
    Integer quantityOnHand,
    @NotNull BigDecimal price,
    LocalDateTime createdDate,
    LocalDateTime updatedDate) {
}

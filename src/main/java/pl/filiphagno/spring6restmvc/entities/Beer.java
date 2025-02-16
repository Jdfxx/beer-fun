package pl.filiphagno.spring6restmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import pl.filiphagno.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    UUID id;

    @Version
    Integer version;

    @NotBlank
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    String beerName;
    @NotNull
    BeerStyle beerStyle;
    @NotBlank
    String upc;
    Integer quantityOnHand;
    @NotNull
    BigDecimal price;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}

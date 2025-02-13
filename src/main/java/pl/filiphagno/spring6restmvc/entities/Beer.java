package pl.filiphagno.spring6restmvc.entities;

import jakarta.persistence.*;
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
    String beerName;
    BeerStyle beerStyle;
    String upc;
    Integer quantityOnHand;
    BigDecimal price;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}

package pl.filiphagno.spring6restmvc.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record Customer(UUID id,
                       String name,
                       Integer version,
                       LocalDateTime created,
                       LocalDateTime updated) {
}

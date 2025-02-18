package pl.filiphagno.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CustomerDTO(UUID id,
                          @NotBlank String name,
                          String email,
                          Integer version,
                          LocalDateTime created,
                          LocalDateTime updated) {
}

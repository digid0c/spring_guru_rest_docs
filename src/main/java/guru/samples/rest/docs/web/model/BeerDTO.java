package guru.samples.rest.docs.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDTO {

    private UUID id;
    private Long version;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;

    @NotBlank
    @Size(min = 1, max = 75)
    private String name;

    @NotNull
    private BeerStyle style;

    @Positive
    @NotNull
    private Long upc;

    @Positive
    @NotNull
    private BigDecimal price;

    @Min(1)
    @Max(300)
    private Integer minOnHand;

    @Min(100)
    @Max(10_000)
    private Integer quantityToBrew;
}

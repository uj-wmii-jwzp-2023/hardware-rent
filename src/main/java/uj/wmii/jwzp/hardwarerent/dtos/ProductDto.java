package uj.wmii.jwzp.hardwarerent.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long id;
    @NotNull
    @NotBlank
    private String companyName;
    @NotNull
    @NotBlank
    private String model;
    @NotNull
    private BigDecimal price;
    @NotNull
    @NotBlank
    private String categoryName;
}

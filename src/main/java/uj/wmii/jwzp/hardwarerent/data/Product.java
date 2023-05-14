package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @NotNull
    @NotBlank
    @Getter @Setter
    private String companyName;
    @Column(unique = true)
    @NotNull
    @NotBlank
    @Getter @Setter
    private String model;
    @NotNull
    @Getter @Setter
    private BigDecimal price;
    @NotNull
    @Getter @Setter
    private Integer availableQuantity;
    @NotNull
    @Getter @Setter
    private Integer overallQuantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    @Getter @Setter
    private Category category;

}

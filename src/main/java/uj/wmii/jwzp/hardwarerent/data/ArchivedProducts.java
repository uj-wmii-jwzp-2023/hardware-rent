package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "archived_products")
@NoArgsConstructor
public class ArchivedProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter @Setter
    private Long productId;
    @Getter @Setter
    private String companyName;
    @Getter @Setter
    private String model;
    @Getter @Setter
    private BigDecimal price;
    @Getter @Setter
    private String categoryName;

    public ArchivedProducts(Product product) {
        this.productId = product.getId();
        this.companyName = product.getCompanyName();
        this.model = product.getModel();
        this.price = product.getPrice();
        this.categoryName = product.getCategory().getCategoryName();
    }

}

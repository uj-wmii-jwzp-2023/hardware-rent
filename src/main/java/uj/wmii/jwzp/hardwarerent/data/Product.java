package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products", schema = "myschema")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String companyName;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String model;
    @Column(nullable = false)
    @Getter @Setter
    private BigDecimal price;
    @Column(nullable = false)
    @Getter @Setter
    private Integer availableQuantity;
    @Column(nullable = false)
    @Getter @Setter
    private Integer overallQuantity;

    @ManyToOne
    @JoinColumn(name="category_id")
    @Getter @Setter
    private Category category;
    public Product(String companyName,
                   String model,
                   Category category,
                   int price,
                   int quantity) {
        this.companyName = companyName;
        this.model = model;
        this.price = new BigDecimal(price);
        this.category = category;
        this.availableQuantity = quantity;
        this.overallQuantity = quantity;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", available=" + availableQuantity +
                ", overall=" + overallQuantity +
                '}';
    }

}

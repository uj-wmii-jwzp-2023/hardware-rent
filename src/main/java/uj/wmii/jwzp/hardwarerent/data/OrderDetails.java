package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details", schema = "myschema")
@NoArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long orderDetails_id;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Getter @Setter
    private Product product;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private int quantity;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String description;
    @ManyToOne(optional=false)
    @JoinColumn(name="id")
    @Getter @Setter
    private Order order;

    public OrderDetails(Product product, int quantity, String description, Order order) {
        this.product = product;
        this.quantity = quantity;
        this.description = description;
        this.order = order;
    }
}

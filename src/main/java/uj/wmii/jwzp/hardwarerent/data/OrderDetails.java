package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details", schema = "myschema")
@NoArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Getter @Setter
    private Product product;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_and_order_details",
            joinColumns = @JoinColumn(
                    name = "order_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "order_id", referencedColumnName = "id"))
    @Getter @Setter
    private Order order;

    public OrderDetails(Product product, int quantity, Order order) {
        this.product = product;
        this.quantity = quantity;
        this.order = order;
    }
}

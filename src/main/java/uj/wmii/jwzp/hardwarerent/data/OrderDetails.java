package uj.wmii.jwzp.hardwarerent.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "order_details")
@NoArgsConstructor
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Getter @Setter
    private ArchivedProducts archivedProducts;
    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String description;
    @ManyToOne
    @JoinTable(
            name = "order_and_order_details",
            joinColumns = @JoinColumn(
                    name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "order_detail_id", referencedColumnName = "id"))
    @Getter @Setter
    @JsonBackReference
    private Order order;

    public OrderDetails(ArchivedProducts archivedProducts, String description, Order order) {
        this.archivedProducts = archivedProducts;
        this.description = (description == null) ? "no description": description;
        this.order = order;
    }
}

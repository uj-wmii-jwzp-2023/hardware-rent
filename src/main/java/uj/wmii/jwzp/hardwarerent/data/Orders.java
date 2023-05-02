package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "orders", schema = "myschema")
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Getter @Setter
    private MyUser user;
    @Column(name = "order_date",nullable = false)
    @Getter @Setter
    private Date orderDate;
    @Column(name = "due_date",nullable = false)
    @Getter @Setter
    private Date dueDate;
    @OneToMany(mappedBy="orders")
    @Getter @Setter
    private Set<OrderDetails> orderDetails;

    public Orders(MyUser user, Date orderDate, Date dueDate) {
        this.user = user;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
    }
}

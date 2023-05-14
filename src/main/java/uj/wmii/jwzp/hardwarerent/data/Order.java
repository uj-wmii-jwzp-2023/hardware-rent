package uj.wmii.jwzp.hardwarerent.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "orders", schema = "myschema")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @ManyToOne(optional=false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @Getter @Setter
    private MyUser user;
    @Column(name = "order_date",nullable = false)
    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date orderDate;
    @Column(name = "due_date",nullable = false)
    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date dueDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    @Getter @Setter
    private Set<OrderDetails> orderDetails;

    public Order(MyUser user, Date orderDate, Date dueDate, Set<OrderDetails> orderDetails) {
        this.user = user;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.orderDetails = orderDetails;
    }

}

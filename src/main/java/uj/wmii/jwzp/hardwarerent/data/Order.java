package uj.wmii.jwzp.hardwarerent.data;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "orders")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate orderDate;
    @Column(name = "due_date",nullable = false)
    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate dueDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    @Getter @Setter
    @JsonManagedReference
    private Set<OrderDetails> orderDetails;

    @Getter
    private BigDecimal overallCashSum;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    public Order(MyUser user, LocalDate orderDate, LocalDate dueDate, Set<OrderDetails> orderDetails) {
        this.user = user;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.orderDetails = orderDetails;
        this.overallCashSum = new BigDecimal("0.00");
        this.orderStatus = OrderStatus.INITIALIZED;
    }

    public void setOverallCashSum() {
        BigDecimal daysBetween = new BigDecimal(Math.abs(Period.between(orderDate, dueDate).getDays()));
        BigDecimal toMultiply = daysBetween.divide(new BigDecimal(7), 2, RoundingMode.CEILING);
        this.overallCashSum = orderDetails.stream()
                .map(x -> x.getArchivedProducts().getPrice().multiply(toMultiply))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

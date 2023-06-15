package uj.wmii.jwzp.hardwarerent.data;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
    private LocalDateTime orderDate;
    @Column(name = "due_date",nullable = false)
    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
    private LocalDateTime dueDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order", fetch = FetchType.EAGER)
    @Getter @Setter
    @JsonManagedReference
    private Set<OrderDetails> orderDetails;

    @CreationTimestamp
    @Getter @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
    LocalDateTime createdOn;
    @Getter
    private BigDecimal overallCashSum;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    public Order(MyUser user, LocalDateTime orderDate, LocalDateTime dueDate, Set<OrderDetails> orderDetails) {
        this.user = user;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.orderDetails = orderDetails;
        this.overallCashSum = new BigDecimal("0.00");
        this.orderStatus = OrderStatus.INITIALIZED;
    }

    public void setOverallCashSum() {
        BigDecimal daysBetween = new BigDecimal(Math.abs(Duration.between(orderDate, dueDate).toHours()));
        BigDecimal toMultiply = daysBetween.divide(new BigDecimal(168), 2, RoundingMode.CEILING);
        this.overallCashSum = orderDetails.stream()
                .map(x -> x.getArchivedProducts().getPrice().multiply(toMultiply))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Order() {
        this.orderDetails = new HashSet<>(); 
    }
}

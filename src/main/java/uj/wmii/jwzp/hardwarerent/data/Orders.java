package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long order_id;

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private MyUser user;
    private Date orderDate;
    private Date dueDate;
    @OneToMany(mappedBy="orders")
    private Set<OrderDetails> orderDetails;

    public Orders(MyUser user, Date orderDate, Date dueDate) {
        this.user = user;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(Set<OrderDetails> orderDetails)
    {
        this.orderDetails = orderDetails;

    }
}

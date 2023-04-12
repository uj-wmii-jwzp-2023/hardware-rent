package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal moneyToPay;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getMoneyToPay() {
        return moneyToPay;
    }

    public void setMoneyToPay(BigDecimal moneyToPay) {
        this.moneyToPay = moneyToPay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyToPay=" + moneyToPay +
                '}';
    }
}

package uj.wmii.jwzp.hardwarerent.data;

import jakarta.persistence.*;

@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderDetails_id;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;
    private int quantity;
    private String description;
    @ManyToOne(optional=false)
    @JoinColumn(name="order_id")
    private Orders orders;

    public OrderDetails(Product product, int quantity, String description, Orders order) {
        this.product = product;
        this.quantity = quantity;
        this.description = description;
        this.orders = order;
    }

    public OrderDetails() {
    }


    public Long getOrderDetails_id() {
        return orderDetails_id;
    }

    public Long getProduct_id() {
        return product.getProduct_id();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Orders getOrder() {
        return orders;
    }

    public void setOrder(Orders order) {
        this.orders = order;
    }
}

package uj.wmii.jwzp.hardwarerental.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String companyName;
    private String model;
    private BigDecimal price;
    private Boolean isAvailable;
    @ManyToOne
    @JoinColumn(name="category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    @JsonBackReference
    private Basket basket;

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", category=" + category +
                ", basket=" + basket +
                '}';
    }
}

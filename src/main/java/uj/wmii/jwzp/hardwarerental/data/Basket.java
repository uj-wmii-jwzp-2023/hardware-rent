package uj.wmii.jwzp.hardwarerental.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "basket", fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Basket basket = (Basket) o;

        return id.equals(basket.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                ", products=" + products +
                '}';
    }
}

package uj.wmii.jwzp.hardwarerental.data;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<Product> products;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", products=" + products +
                '}';
    }
}

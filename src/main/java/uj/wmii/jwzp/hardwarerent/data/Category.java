package uj.wmii.jwzp.hardwarerent.data;


import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long category_id;
    private String categoryName;

    @OneToMany(mappedBy="category")
    private Set<Product> products;

    public Long getCategory_id() {
        return category_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setProducts(Set<Product> _products)
    {
        this.products = _products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return category_id.equals(category.category_id);
    }

    @Override
    public int hashCode() {
        return category_id.hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + category_id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}

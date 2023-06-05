package uj.wmii.jwzp.hardwarerent.data;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.dtos.CategoryDto;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Column(nullable = false, length = 100)
    @NotNull
    @Getter @Setter
    private String categoryName;

    @OneToMany(mappedBy="category")
    @Getter @Setter
    @JsonManagedReference
    private Set<Product> products;

    public Category(CategoryDto categoryDto){
        this.categoryName = categoryDto.getCategoryName();
        this.products = new HashSet<>();
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.products = new HashSet<>();
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
                '}';
    }
}

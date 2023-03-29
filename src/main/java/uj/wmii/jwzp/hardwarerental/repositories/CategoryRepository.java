package uj.wmii.jwzp.hardwarerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT p from Product p where p.category.category_id = ?1")
    List<Product> findProductByCategoryId(Long categoryId);
}

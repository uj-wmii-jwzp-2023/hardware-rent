package uj.wmii.jwzp.hardwarerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT p from Product p where p.category.id = ?1")
    List<Product> findProductByCategoryId(Long categoryId);
}

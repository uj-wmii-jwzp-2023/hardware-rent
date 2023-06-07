package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uj.wmii.jwzp.hardwarerent.data.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByModel(String model);

    List<Product> findAllByCompanyNameIsLikeIgnoreCase(String companyName);

    List<Product> findAllByPriceLessThanEqual(BigDecimal price);

    List<Product> findAllByCategoryCategoryName(String categoryName);
}

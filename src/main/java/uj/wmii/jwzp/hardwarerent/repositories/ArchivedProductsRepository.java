package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.ArchivedProducts;

import java.util.Optional;

public interface ArchivedProductsRepository extends JpaRepository<ArchivedProducts, Long> {
    Optional<ArchivedProducts> findArchivedProductsByProductId(Long id);
}

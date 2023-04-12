package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

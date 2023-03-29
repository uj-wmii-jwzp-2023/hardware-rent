package uj.wmii.jwzp.hardwarerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uj.wmii.jwzp.hardwarerental.data.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

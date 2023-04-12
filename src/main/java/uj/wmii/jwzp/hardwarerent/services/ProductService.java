package uj.wmii.jwzp.hardwarerent.services;


import uj.wmii.jwzp.hardwarerent.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createNewProduct(Product product);

    Product updateWholeProductById(Long id, Product product);

    void deleteProductById(Long id);

    void updatePartOfProductById(Long id, Product product);

    List<Product> getProductsByAvailability(Boolean available);
}

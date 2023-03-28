package uj.wmii.jwzp.hardwarerental.services;


import uj.wmii.jwzp.hardwarerental.data.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Product addNewProduct(Product product);

    Optional<Product> getProductById(Long id);

    Product updateWholeProductById(Long id, Product product);

    Product updatePartOfProductById(Long id, Product product);

    void deleteProductById(Long id);

    List<Product> returnProductsByAvailability(Boolean availability);

}

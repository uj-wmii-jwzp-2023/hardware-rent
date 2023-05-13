package uj.wmii.jwzp.hardwarerent.services.interfaces;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.data.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product createNewProduct(Product product);

    Product updateWholeProductById(Long id, Product product);
    Optional<Product> getProductByModel(String model);

    void deleteProductById(Long id);

    Product updatePartOfProductById(Long id, Product product);
    List<ProductDto> getProductDtoList(List<Product> products);

}

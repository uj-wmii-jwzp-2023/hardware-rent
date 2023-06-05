package uj.wmii.jwzp.hardwarerent.services.interfaces;
import uj.wmii.jwzp.hardwarerent.data.ArchivedProducts;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.dtos.ProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDto> getAllProducts(String companyName, BigDecimal price, String categoryName);

    Optional<ProductDto> getProductById(Long id);

    Optional<ProductDto> createNewProduct(ProductDto productDto);

    Optional<Product> updateWholeProductById(Long id, ProductDto productDto);
    Optional<Product> getProductByModel(String model);

    Boolean deleteProductById(Long id);

    Optional<Product> updatePartOfProductById(Long id, ProductDto productDto);

    List<ArchivedProducts> getAllArchivedProducts();

}

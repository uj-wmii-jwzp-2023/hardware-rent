package uj.wmii.jwzp.hardwarerental.services;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product addNewProduct(Product product) {

        Product myProduct = new Product();
        myProduct.setAvailable(product.isAvailable());
        myProduct.setCompanyName(product.getCompanyName());
        myProduct.setModel(product.getModel());
        myProduct.setPrice(product.getPrice());

        Product savedProduct = productRepository.save(myProduct);

        return savedProduct;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateWholeProductById(Long id, Product product) {

        Product existingProduct = productRepository.getById(id);

        existingProduct.setPrice(product.getPrice());
        existingProduct.setAvailable(product.isAvailable());
        existingProduct.setModel(product.getModel());
        existingProduct.setCompanyName(product.getCompanyName());

        Product savedProduct = productRepository.save(existingProduct);

        return savedProduct;
    }

    @Override
    public Product updatePartOfProductById(Long id, Product product) {

        Product existingProduct = productRepository.getById(id);

        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.isAvailable() != null) {
            existingProduct.setAvailable(product.isAvailable());
        }
        if (StringUtils.hasText(product.getModel())) {
            existingProduct.setModel(product.getModel());
        }
        if (StringUtils.hasText(product.getCompanyName())) {
            existingProduct.setCompanyName(product.getCompanyName());
        }

        Product savedProduct = productRepository.save(existingProduct);

        return savedProduct;

    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> returnProductsByAvailability(Boolean availability) {

        Product productWithAvailability = new Product();
        productWithAvailability.setAvailable(availability);

        var exampleProduct = Example.of(productWithAvailability);

        var results = productRepository.findAll(exampleProduct);

        return results;
    }


}

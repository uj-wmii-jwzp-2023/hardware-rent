package uj.wmii.jwzp.hardwarerent.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.repositories.ProductRepository;

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
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createNewProduct(Product product) {

        Product savedProduct = new Product();

        savedProduct.setCompanyName(product.getCompanyName());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setModel(product.getModel());
        savedProduct.setAvailableQuantity(product.getAvailableQuantity());
        savedProduct.setOverallQuantity(product.getOverallQuantity());

        return productRepository.save(savedProduct);
    }

    @Override
    public Product updateWholeProductById(Long id, Product product) {
        Product existing = productRepository.findById(id).orElseThrow();

        existing.setAvailableQuantity(product.getAvailableQuantity());
        existing.setOverallQuantity(product.getOverallQuantity());
        existing.setModel(product.getModel());
        existing.setPrice(product.getPrice());
        existing.setCompanyName(product.getCompanyName());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updatePartOfProductById(Long id, Product product) {
        Product existing = productRepository.findById(id).orElseThrow();

        if (StringUtils.hasText(product.getCompanyName())) {
            existing.setCompanyName(product.getCompanyName());
        }
        if (StringUtils.hasText(product.getModel())) {
            existing.setModel(product.getModel());
        }

        if (product.getPrice() != null) {
            existing.setPrice(product.getPrice());
        }

        existing.setAvailableQuantity(product.getAvailableQuantity());
        existing.setOverallQuantity(product.getOverallQuantity());

        productRepository.save(existing);
    }

}

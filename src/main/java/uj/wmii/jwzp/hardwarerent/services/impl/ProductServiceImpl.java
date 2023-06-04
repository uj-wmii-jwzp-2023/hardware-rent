package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uj.wmii.jwzp.hardwarerent.data.ArchivedProducts;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.dtos.ProductDto;
import uj.wmii.jwzp.hardwarerent.mappers.ProductMapper;
import uj.wmii.jwzp.hardwarerent.repositories.ArchivedProductsRepository;
import uj.wmii.jwzp.hardwarerent.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerent.repositories.ProductRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    private final ArchivedProductsRepository archivedProductsRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              CategoryRepository categoryRepository,
                              ArchivedProductsRepository archivedProductsRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.archivedProductsRepository = archivedProductsRepository;
    }

    @Override
    public List<ProductDto> getAllProducts(String companyName,
                                           BigDecimal price,
                                           Integer availableQuantity,
                                           Integer overallQuantity,
                                           String categoryName) {

        List<Product> toReturn;
        if (StringUtils.hasText(companyName)) {
            toReturn = listAllByCompanyName(companyName);
        }
        else if(!StringUtils.hasText(companyName) && price != null) {
            toReturn = listAllByPriceSmallerThan(price); 
        }
        else if(!StringUtils.hasText(companyName) && price == null && availableQuantity != null) {
            toReturn = listAllByAvailableQuantityBiggerThan(availableQuantity); 
        }
        else if(!StringUtils.hasText(companyName) && price == null && availableQuantity == null && overallQuantity != null) {
            toReturn = listAllByOverallQuantityBiggerThan(overallQuantity); 
        }
        else if(!StringUtils.hasText(companyName) && price == null
                && availableQuantity == null && overallQuantity == null && StringUtils.hasText(categoryName)){
            toReturn = listAllByCategoryName(categoryName);
        }
        else {
            toReturn = productRepository.findAll();
        }
        return toReturn.stream().
                map(productMapper::productToProductDto)
                .toList();
    }

    private List<Product> listAllByCategoryName(String categoryName) {
        return productRepository.findAllByCategoryCategoryName(categoryName);
    }

    private List<Product> listAllByOverallQuantityBiggerThan(Integer overallQuantity) {
        return productRepository.findAllByOverallQuantityGreaterThanEqual(overallQuantity);
    }

    private List<Product> listAllByAvailableQuantityBiggerThan(Integer availableQuantity) {
        return productRepository.findAllByAvailableQuantityGreaterThanEqual(availableQuantity);
    }

    private List<Product> listAllByPriceSmallerThan(BigDecimal price) {
        return productRepository.findAllByPriceLessThanEqual(price);
    }

    public List<Product> listAllByCompanyName(String companyName) {
        return productRepository.findAllByCompanyNameIsLikeIgnoreCase("%" + companyName + "%");
    }

    @Override
    public Optional<ProductDto> getProductById(Long id) {
        return Optional.ofNullable(productMapper.productToProductDto(
                productRepository.findById(id).orElse(null)
        ));
    }

    @Override
    public Optional<ProductDto> createNewProduct(ProductDto productDto) {
        if (categoryRepository.findCategoryByCategoryName(productDto.getCategoryName()).isEmpty())
            return Optional.empty();

        Product newProduct = productMapper.productDtoToProduct(productDto);
        newProduct.setCategory(categoryRepository.findCategoryByCategoryName(productDto.getCategoryName()).orElse(null));
        ArchivedProducts archivedProduct = new ArchivedProducts(newProduct);
        archivedProductsRepository.save(archivedProduct);
        return Optional.of(productMapper.productToProductDto(productRepository.save(newProduct)));
    }

    @Override
    public Optional<Product> updateWholeProductById(Long id, ProductDto productDto) {
        Product existing = productRepository.findById(id).get();
        ArchivedProducts archivedExisting = archivedProductsRepository.findArchivedProductsByProductId(id).get();
        existing.setAvailableQuantity(productDto.getAvailableQuantity());
        existing.setOverallQuantity(productDto.getOverallQuantity());
        existing.setModel(productDto.getModel());
        existing.setPrice(productDto.getPrice());
        existing.setCompanyName(productDto.getCompanyName());
        if (categoryRepository.findCategoryByCategoryName(productDto.getCategoryName()).isPresent()
        && !categoryRepository.findCategoryByCategoryName(productDto.getCategoryName()).get()
                .getCategoryName().equals(productDto.getCategoryName()))
            return Optional.empty();

        archivedExisting.setCompanyName(productDto.getCompanyName());
        archivedExisting.setModel(productDto.getModel());
        archivedExisting.setPrice(productDto.getPrice());

        return Optional.of(productRepository.save(existing));
    }

    @Override
    public Optional<Product> getProductByModel(String model) {
        return productRepository.findByModel(model);
    }

    @Override
    public Boolean deleteProductById(Long id) {
        if (!productRepository.existsById(id))
            return false;
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Product> updatePartOfProductById(Long id, ProductDto productDto) {
        Product existing = productRepository.findById(id).get();
        ArchivedProducts archivedExisting = archivedProductsRepository.findArchivedProductsByProductId(id).get();

        if (StringUtils.hasText(productDto.getCompanyName())) {
            existing.setCompanyName(productDto.getCompanyName());
            archivedExisting.setCompanyName(productDto.getCompanyName());
        }
        if (StringUtils.hasText(productDto.getModel())) {
            existing.setModel(productDto.getModel());
            archivedExisting.setModel(productDto.getModel());
        }
        if (productDto.getPrice() != null) {
            existing.setPrice(productDto.getPrice());
            archivedExisting.setPrice(productDto.getPrice());
        }
        if (productDto.getOverallQuantity() != null) {
            existing.setOverallQuantity(productDto.getOverallQuantity());
        }
        if (productDto.getAvailableQuantity() != null) {
            existing.setAvailableQuantity(productDto.getAvailableQuantity());
        }
        if (StringUtils.hasText(productDto.getCategoryName())) {
            if (categoryRepository.findCategoryByCategoryName(productDto.getCategoryName()).isPresent()
                    && !categoryRepository.findCategoryByCategoryName(productDto.getCategoryName()).get()
                    .getCategoryName().equals(productDto.getCategoryName()))
                return Optional.empty();
        }
        return Optional.of(productRepository.save(existing));
    }

    @Override
    public List<ArchivedProducts> getAllArchivedProducts() {
        return archivedProductsRepository.findAll();
    }

}

package uj.wmii.jwzp.hardwarerent.mappers;

import org.springframework.stereotype.Component;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.dtos.ProductDto;

@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public ProductDto productToProductDto(Product product) {
        if (product == null)
            return null;

        return ProductDto.builder()
                .id(product.getId())
                .companyName(product.getCompanyName())
                .model(product.getModel())
                .price(product.getPrice())
                .availableQuantity(product.getAvailableQuantity())
                .overallQuantity(product.getOverallQuantity())
                .categoryName(product.getCategory().getCategoryName())
                .build();
    }

    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        if (productDto == null)
            return null;

        return Product.builder()
                .id(productDto.getId())
                .companyName(productDto.getCompanyName())
                .model(productDto.getModel())
                .price(productDto.getPrice())
                .availableQuantity(productDto.getAvailableQuantity())
                .overallQuantity(productDto.getOverallQuantity())
                .category(null)
                .build();
    }
}

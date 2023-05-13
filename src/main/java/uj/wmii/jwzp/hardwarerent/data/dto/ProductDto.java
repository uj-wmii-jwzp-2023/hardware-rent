package uj.wmii.jwzp.hardwarerent.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ProductDto {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String companyName;
    @Getter @Setter
    private String model;
    @Getter @Setter
    private BigDecimal price;
    @Getter @Setter
    private Integer availableQuantity;
    @Getter @Setter
    private Integer overallQuantity;
    @Getter @Setter
    private String categoryName;
    public ProductDto(Product product) {
        this.id = product.getId();
        this.companyName = product.getCompanyName();
        this.model = product.getModel();
        this.price = product.getPrice();
        this.availableQuantity = product.getAvailableQuantity();
        this.overallQuantity = product.getOverallQuantity();
        this.categoryName = product.getCategory().getCategoryName();
    }

}

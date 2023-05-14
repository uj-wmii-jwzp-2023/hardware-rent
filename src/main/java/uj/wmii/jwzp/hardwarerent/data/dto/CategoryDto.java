package uj.wmii.jwzp.hardwarerent.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.Category;

import java.util.Set;

public class CategoryDto {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String categoryName;
    @JsonIgnore
    @Getter @Setter
    private Set<ProductDto> productDto;
    public CategoryDto(Category category){
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }
}

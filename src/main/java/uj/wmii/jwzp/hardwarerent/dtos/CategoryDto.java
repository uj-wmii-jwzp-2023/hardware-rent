package uj.wmii.jwzp.hardwarerent.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.Category;

import java.util.HashSet;
import java.util.Set;

public class CategoryDto {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String categoryName;
    @JsonIgnore
    @Getter @Setter
    private Set<ProductDto> productDto = new HashSet<>();
    public CategoryDto(Category category){
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }
}

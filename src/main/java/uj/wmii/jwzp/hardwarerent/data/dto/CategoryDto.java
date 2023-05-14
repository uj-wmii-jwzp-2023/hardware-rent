package uj.wmii.jwzp.hardwarerent.data.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.Category;

public class CategoryDto {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String categoryName;
    public CategoryDto(Category category){
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }
}
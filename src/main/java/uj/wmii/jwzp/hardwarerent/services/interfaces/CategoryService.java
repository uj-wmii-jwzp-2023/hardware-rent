package uj.wmii.jwzp.hardwarerent.services.interfaces;

import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.dtos.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);
    Optional<Category> getCategoryByName(String name);
    Category createNewCategory(Category category);
    List<CategoryDto> getCategoryDtoList(List<Category> categories);
    void deleteCategoryById(Long id);
    Category createCategoryFromDto(CategoryDto categoryDto);

}

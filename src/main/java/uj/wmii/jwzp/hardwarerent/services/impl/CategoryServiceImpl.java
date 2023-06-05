package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.dtos.CategoryDto;
import uj.wmii.jwzp.hardwarerent.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    @Override
    public boolean deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id))
            return false;
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public Category createCategoryFromDto(CategoryDto categoryDto) {
        return new Category(categoryDto);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findCategoryByCategoryName(name);
    }


    @Override
    public Optional<Category> createNewCategory(String categoryName) {

        if (categoryRepository.findCategoryByCategoryName(categoryName).isPresent())
            return Optional.empty();
        Category newCategory = new Category(categoryName); // to exclude option with existing id
        return Optional.of(categoryRepository.save(newCategory));
    }
    @Override
    public List<CategoryDto> getCategoryDtoList(List<Category> categories){
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (var category: categories) {
            categoryDtoList.add(new CategoryDto(category));
        }
        return categoryDtoList;
    }

}

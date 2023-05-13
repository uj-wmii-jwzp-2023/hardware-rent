package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.data.dto.CategoryDto;
import uj.wmii.jwzp.hardwarerent.data.dto.ProductDto;
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
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findCategoryByCategoryName(name);
    }


    @Override
    public Category createNewCategory(Category category)
    {
        category.setId(null); // to exclude option with existing id
        return categoryRepository.save(category);
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

package uj.wmii.jwzp.hardwarerental.services;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.repositories.CategoryRepository;

import java.util.List;

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
    public Category addNewCategory(Category category) {

        Category myCategory = new Category();
        myCategory.setCategoryName(category.getCategoryName());

        Category savedCategory = categoryRepository.save(myCategory);

        return savedCategory;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return categoryRepository.findProductByCategoryId(categoryId);
    }
}

package uj.wmii.jwzp.hardwarerent.services;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.models.Category;
import uj.wmii.jwzp.hardwarerent.repositories.CategoryRepository;

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
        return Optional.empty();
    }
}

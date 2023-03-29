package uj.wmii.jwzp.hardwarerental.services;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category addNewCategory(Category category);

    List<Product> getProductsByCategoryId(Long categoryId);
}

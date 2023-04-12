package uj.wmii.jwzp.hardwarerent.services;

import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Product;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category addNewCategory(Category category);

    List<Product> getProductsByCategoryId(Long categoryId);
}

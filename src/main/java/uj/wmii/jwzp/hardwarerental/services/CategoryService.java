package uj.wmii.jwzp.hardwarerental.services;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerental.data.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category addNewCategory(Category category);


}

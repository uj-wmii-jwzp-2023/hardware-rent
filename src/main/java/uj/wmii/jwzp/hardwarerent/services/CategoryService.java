package uj.wmii.jwzp.hardwarerent.services;

import uj.wmii.jwzp.hardwarerent.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

}

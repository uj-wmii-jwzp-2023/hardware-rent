package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.exceptions.AlreadyExistsException;
import uj.wmii.jwzp.hardwarerent.exceptions.CategoryRemovalException;
import uj.wmii.jwzp.hardwarerent.exceptions.NotFoundException;
import uj.wmii.jwzp.hardwarerent.services.interfaces.CategoryService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        List<Category> categoriesReturned;
        categoriesReturned = categoryService.getAllCategories();
        return categoriesReturned;
    }

    @PostMapping
    public ResponseEntity createNewCategory(@RequestBody String categoryName) {
        Optional<Category> savedCategory = categoryService.createNewCategory(categoryName);
        if (savedCategory.isEmpty())
            throw new AlreadyExistsException("Category with such name already exists!");
        return ResponseEntity.created(URI.create("/categories/" + savedCategory.get().getId()))
                .body("Category has been successfully created!");
    }
    @GetMapping("{id}")
    public Category getCategoryById(@PathVariable Long id)
    {
        return categoryService.getCategoryById(id).orElseThrow(
                () -> new NotFoundException("Failed to find category with id: " + id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") Long id) {
        if (categoryService.getCategoryById(id).isPresent() &&
        !categoryService.getCategoryById(id).get().getProducts().isEmpty())
            throw new CategoryRemovalException("Cannot delete category. Please delete all products from the category first!");
        if (!categoryService.deleteCategoryById(id))
            throw new NotFoundException("Failed to find category with id: " + id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}

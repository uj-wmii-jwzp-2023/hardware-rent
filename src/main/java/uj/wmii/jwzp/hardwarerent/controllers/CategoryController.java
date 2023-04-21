package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.services.CategoryService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity getAllCategories() {
        //return categoryService.getAllCategories();
        var categoriesReturned = categoryService.getAllCategories();
        if (categoriesReturned == null)
            return ResponseEntity.status(404).body("Error while getting all categories");
        else
            return ResponseEntity.ok().body(categoriesReturned);
    }

    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody Category category) {

        Category savedCategory = categoryService.createNewCategory(category);
        if (savedCategory == null)
            return ResponseEntity.status(404).body("Error while creating new categorie");
        else
            return ResponseEntity.created(URI.create("/categories/" + savedCategory.getCategory_id()))
                .body("Product has been successfully created!");
    }

}

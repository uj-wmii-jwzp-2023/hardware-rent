package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity addNewCategory(Category category) {

        Category savedCategory = categoryService.addNewCategory(category);
//        String categoryId = category.getId().toString();

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "/categories" + categoryId);

        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping("{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getProductsByCategoryId(categoryId);
    }

}

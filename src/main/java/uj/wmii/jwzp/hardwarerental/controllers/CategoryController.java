package uj.wmii.jwzp.hardwarerental.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerental.services.CategoryService;
import uj.wmii.jwzp.hardwarerental.services.CategoryServiceImpl;

import java.util.List;
import java.util.Optional;

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

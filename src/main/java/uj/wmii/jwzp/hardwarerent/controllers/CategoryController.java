package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.services.interfaces.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity getAllCategories()
    {
        List<Category> categoriesReturned;
        try {
            categoriesReturned = categoryService.getAllCategories();
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to get all categories");
        return ResponseEntity.ok().body(categoriesReturned);

    }

    @PostMapping
    public ResponseEntity createNewCategory(@RequestBody Category category) {
        Category savedCategory;
        try {
            if(categoryService.getCategoryByName(category.getCategoryName()).isPresent()){
                LOG.info("Received request to add category with already existing name");
                return ResponseEntity.badRequest().body("Category with such name already existed");
            }
            savedCategory = categoryService.createNewCategory(category);
            if (savedCategory == null){
                LOG.error("internal server error while creating new category");
                return ResponseEntity.internalServerError().body("Error while creating new category");
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to create new category");
        return ResponseEntity.created(URI.create("/categories/" + savedCategory.getId()))
                .body("Category has been successfully created!");
    }
    @GetMapping("{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id)
    {
        Optional categoryReturned;
        try{
            categoryReturned = categoryService.getCategoryById(id);
            if(categoryReturned.isEmpty()) {
                LOG.info("Failed to find category with id: '{}'",id);
                return ResponseEntity.status(404).body("Failed to find category with id: "+id);
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error");
        }
        LOG.info("Proceeded request to get category with id: '{}'",id);
        return ResponseEntity.ok().body(categoryReturned);
    }

}

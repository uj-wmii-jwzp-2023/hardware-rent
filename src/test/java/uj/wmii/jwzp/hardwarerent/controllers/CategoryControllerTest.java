package uj.wmii.jwzp.hardwarerent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.exceptions.AlreadyExistsException;
import uj.wmii.jwzp.hardwarerent.exceptions.NotFoundException;
import uj.wmii.jwzp.hardwarerent.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerent.repositories.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CategoryControllerTest {
    @Autowired
    CategoryController categoryController;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void testAllCategories() {
        List<Category> categories = categoryController.getAllCategories();

        assertEquals(2, categories.size());
    }

    @Test
    void testGetCategoryById() {
        Category testCategory = categoryRepository.findAll().get(0);

        Category category = categoryController.getCategoryById(testCategory.getId());

        assertNotNull(category);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        assertThrows(NotFoundException.class,
                () -> categoryController.getCategoryById(100L));
    }

    @Transactional
    @Rollback
    @Test
    void testPostCategoryWithValidProperties() {
        final String categoryName = "Phones";

        var responseEntity = categoryController.createNewCategory(categoryName);
        assertEquals(HttpStatusCode.valueOf(201), responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders());


        assertNotNull(categoryRepository.findCategoryByCategoryName(categoryName));
        assertNotNull(categoryRepository.findCategoryByCategoryName(categoryName).get().getId());
    }

    @Test
    void testPostCategoryWithExistingName() {
        final String categoryName = "Laptop";

        assertThrows(AlreadyExistsException.class,
                () -> categoryController.createNewCategory(categoryName));
    }

    @Test
    void testDeleteCategoryWithProducts() throws Exception {
        mockMvc.perform(delete("/categories/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testDeleteCategoryNotExists() {
        assertThrows(NotFoundException.class,
                () -> categoryController.deleteProductById(100L));
    }


}
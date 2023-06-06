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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.dtos.ProductDto;
import uj.wmii.jwzp.hardwarerent.exceptions.AlreadyExistsException;
import uj.wmii.jwzp.hardwarerent.exceptions.NotFoundException;
import uj.wmii.jwzp.hardwarerent.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerent.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProductControllerIntegrationTest {

    @Autowired
    ProductController productController;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

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
    void testGetAllProducts() {
        List<ProductDto> dtos = productController.getAllProducts(null, null, null);

        assertEquals(5, dtos.size());
    }

    @Test
    void testGetProductById() {
        Product product = productRepository.findAll().get(0);

        ProductDto productDto = productController.getProductById(product.getId());

        System.out.println(productDto.toString());
        assertNotNull(productDto);
    }

    @Test
    void testGetProductByIdNotFound() {
        assertThrows(NotFoundException.class,
                () -> productController.getProductById(100L));
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteExistingProduct() {
        Product testProduct = productRepository.findAll().get(0);

        Long id = testProduct.getId();

        var responseEntity = productController.deleteProductById(id);
        assertFalse(productRepository.existsById(id));
        assertEquals(HttpStatusCode.valueOf(204), responseEntity.getStatusCode());
    }

    @Test
    void testDeleteProductNotFound() {
        assertThrows(NotFoundException.class,
                () -> productController.deleteProductById(7L));
    }

    @Test
    void testGetProductByCompanyName() throws Exception {
        mockMvc.perform(get("/products")
                .queryParam("companyName", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetProductByPrice() throws Exception {
        mockMvc.perform(get("/products")
                .queryParam("price", "704"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }


    @Test
    void testGetProductByCategoryName() throws Exception {
        mockMvc.perform(get("/products")
                        .queryParam("categoryName", "TV"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    @Transactional
    @Rollback
    void testPostProductWithValidProperties() throws Exception {
        ProductDto productToAdd = ProductDto.builder()
                .companyName("Samsung")
                .price(new BigDecimal("333.44"))
                .model("neeeeewwww")
                .categoryName("TV")
                .build();

        var responseEntity = productController.createNewProduct(productToAdd);
        assertEquals(HttpStatusCode.valueOf(201), responseEntity.getStatusCode());
        assertNotNull(responseEntity.getHeaders());

        String getLocation = String.valueOf(responseEntity.getHeaders().getLocation());
        Long id = Long.parseLong(String.valueOf(getLocation.charAt(getLocation.length() - 1)));

        assertNotNull(productRepository.findById(id));
        assertNotNull(productRepository.findById(id).get().getPrice());

    }

    @Test
    void testPostProductWithNotExistingCategory() {
        ProductDto productToAdd = ProductDto.builder()
                .companyName("Samsung")
                .price(new BigDecimal("333.44"))
                .model("neeeeewwww")
                .categoryName("TV11111")
                .build();

        assertThrows(NotFoundException.class, () ->
                productController.createNewProduct(productToAdd));
    }

    @Test
    void testPostAlreadyExistingProduct() throws Exception{
        ProductDto productToAdd = ProductDto.builder()
                .companyName("Samsung")
                .price(new BigDecimal("333.44"))
                .model("tv2000")
                .categoryName("TV")
                .build();

        assertThrows(AlreadyExistsException.class,
                () -> productController.createNewProduct(productToAdd));
    }

    @Test
    @Transactional
    @Rollback
    void testPutProductWithValidProperties() {
        ProductDto productToAdd = ProductDto.builder()
                .companyName("Samsung")
                .price(new BigDecimal("333.44"))
                .model("neeeeewwww")
                .categoryName("TV")
                .build();

        var responseEntity = productController.updateWholeProductById(1L, productToAdd);
        assertEquals(HttpStatusCode.valueOf(204), responseEntity.getStatusCode());

        Product savedProduct = productRepository.findById(1L).orElse(null);

        assertNotNull(savedProduct);
        assertEquals("neeeeewwww", savedProduct.getModel());
    }

    @Test
    void testPutWithNonExistingId() {
        ProductDto productToAdd = ProductDto.builder()
                .companyName("Samsung")
                .price(new BigDecimal("333.44"))
                .model("neeeeewwww")
                .categoryName("TV")
                .build();

        assertThrows(NotFoundException.class,
                () -> productController.updateWholeProductById(100L, productToAdd));
    }

    @Test
    void testPostWithNotValidProperties() throws Exception {
        ProductDto productToAdd = ProductDto.builder()
                .price(new BigDecimal("333.44"))
                .model("neeeeewwww")
                .categoryName("TV")
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size()", is(2)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}
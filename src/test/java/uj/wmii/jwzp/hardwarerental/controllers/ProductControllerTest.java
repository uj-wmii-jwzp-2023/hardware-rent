package uj.wmii.jwzp.hardwarerental.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.services.ProductService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ProductService productService;
    Product firstProduct = new Product();

    Product secondProduct = new Product();
    @BeforeEach
    void setUp() {
        firstProduct.setModel("Lattitude 5590");
        firstProduct.setId(1L);
        firstProduct.setCompanyName("Dell");
        firstProduct.setPrice(new BigDecimal("1000"));
        firstProduct.setAvailable(true);

        secondProduct.setModel("Galaxy S50");
        secondProduct.setId(2L);
        secondProduct.setCompanyName("Samsung");
        secondProduct.setPrice(new BigDecimal(2000));
        secondProduct.setAvailable(true);
    }

    @Test
    void testGetAllProductsReturnsListOfAllProducts() throws Exception {
        var allProducts = new ArrayList<>(Arrays.asList(firstProduct, secondProduct));
        Mockito.when(productService.getAllProducts()).thenReturn(allProducts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].companyName", Matchers.is("Samsung")))
                .andExpect(jsonPath("$[0].model", Matchers.is("Lattitude 5590")));

    }

    @Test
    void testGetProductByIdReturnsProductWithWantedId() throws Exception {
        var allProducts = new ArrayList<>(Arrays.asList(firstProduct, secondProduct));

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.ofNullable(firstProduct));

        Mockito.when(productService.getProductById(2L)).thenReturn(Optional.ofNullable(secondProduct));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.companyName", Matchers.is("Dell")))
                .andExpect(jsonPath("$.model", Matchers.is("Lattitude 5590")));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.companyName", Matchers.is("Samsung")))
                .andExpect(jsonPath("$.model", Matchers.is("Galaxy S50")));

    }

    @Test
    void addNewProduct() {
    }

    @Test
    void deleteProductById() {

    }

    @Test
    void updateWholeProductById() {
    }

    @Test
    void updatePartOfProductById() {

    }

    @Test
    void returnProductByAvailability() {
    }
}
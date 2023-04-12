package uj.wmii.jwzp.hardwarerent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.services.ProductService;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;


    Product productFirst = new Product();

    Product productSecond = new Product();

    List<Product> myProducts = new ArrayList<>();

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<Long> idArgumentCaptor;

    @Captor
    ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    public void setUp() {
        productFirst.setProduct_id(1L);
        productFirst.setPrice(new BigDecimal("135.23"));
        productFirst.setCompanyName("Samsung");
        productFirst.setAvailable(true);
        productFirst.setModel("Galaxy A32");

        productSecond.setProduct_id(2L);
        productSecond.setPrice(new BigDecimal("753.34"));
        productSecond.setCompanyName("Dell");
        productSecond.setAvailable(false);
        productSecond.setModel("Latitude 5590");

        myProducts.add(productFirst);
        myProducts.add(productSecond);
    }

    @Test
    void testGetAllProducts() throws Exception {
        given(productService.getAllProducts()).willReturn(myProducts);

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].companyName", is("Samsung")))
                .andExpect(jsonPath("$[1].model", is("Latitude 5590")));
    }

    @Test
    void testGetProductById() throws Exception {
        given(productService.getProductById(1L)).willReturn(Optional.of(myProducts.get(1)));

        mockMvc.perform(get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product_id", is(2)))
                .andExpect(jsonPath("$.model", is("Latitude 5590")))
                .andExpect(jsonPath("$.companyName", is("Dell")));
    }

    @Test
    void testCreateNewProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setProduct_id(3L);
        newProduct.setModel("IPhone XIV");
        newProduct.setAvailable(true);
        newProduct.setPrice(new BigDecimal("1400.34"));
        newProduct.setCompanyName("Apple");

        given(productService.createNewProduct(any(Product.class))).willReturn(newProduct);

        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().string("Product has been successfully created!"));
    }

    @Test
    void testUpdateWholeProductById() throws Exception {
        given(productService.updateWholeProductById(any(Long.class), any(Product.class))).willReturn(productFirst);

        mockMvc.perform(put("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productFirst)))
                .andExpect(status().isNoContent())
                .andExpect(header().exists("Location"));

        verify(productService).updateWholeProductById(any(Long.class), any(Product.class));
    }

    @Test
    void testDeleteProductById() throws Exception {
        mockMvc.perform(delete("/products/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService).deleteProductById(idArgumentCaptor.capture());

        assertEquals(1, idArgumentCaptor.getValue());
    }

    @Test
    void testUpdatePartOfProduct() throws Exception {

        Map<String, Object> shortTermMap = new HashMap<>();
        shortTermMap.put("companyName", "Lenovo");
        shortTermMap.put("available", false);

        mockMvc.perform(patch("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortTermMap)))
                .andExpect(status().isNoContent());

        verify(productService).updatePartOfProductById(idArgumentCaptor.capture(), productArgumentCaptor.capture());

        assertEquals(1L, idArgumentCaptor.getValue());
        assertEquals(productArgumentCaptor.getValue().getCompanyName(), "Lenovo");
        assertFalse(productArgumentCaptor.getValue().isAvailable());
    }

    @Test
    void testGetProductsByAvailability() throws Exception {

        List<Product> trueAvailabilityProducts = new ArrayList<>();

        trueAvailabilityProducts.add(myProducts.get(0));
        given(productService.getProductsByAvailability(true)).willReturn(trueAvailabilityProducts);

        mockMvc.perform(get("/products?availability=true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].product_id", is(1)))
                .andExpect(jsonPath("$[0].available", is(true)));
    }
}
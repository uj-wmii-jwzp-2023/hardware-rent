package uj.wmii.jwzp.hardwarerent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.dtos.ProductDto;
import uj.wmii.jwzp.hardwarerent.mappers.ProductMapper;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ProductService productService;


    ProductDto productFirst = new ProductDto();

    ProductDto productSecond = new ProductDto();
    Product update = new Product();

    List<ProductDto> myProducts = new ArrayList<>();

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<Long> idArgumentCaptor;

    @Captor
    ArgumentCaptor<ProductDto> productArgumentCaptor;


    @BeforeEach
    public void setUp() {
        productFirst.setId(1L);
        productFirst.setPrice(new BigDecimal("135.23"));
        productFirst.setCompanyName("Samsung");
        productFirst.setCategoryName("TV");
        productFirst.setModel("Galaxy A32");

        productSecond.setId(2L);
        productSecond.setPrice(new BigDecimal("753.34"));
        productSecond.setCompanyName("Dell");
        productSecond.setModel("Latitude 5590");

        update.setId(1L);
        update.setPrice(new BigDecimal("3333.22"));
        update.setCompanyName("Dell");
        update.setModel("Longtitude");


        myProducts.add(productFirst);
        myProducts.add(productSecond);
    }

    @Test
    @WithMockUser("admin")
    void testGetAllProducts() throws Exception {
        given(productService.getAllProducts(null, null, null)).willReturn(myProducts);

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].companyName", is("Samsung")))
                .andExpect(jsonPath("$[1].model", is("Latitude 5590")));
    }

    @Test
    @WithMockUser("admin")
    void testGetProductById() throws Exception {
        given(productService.getProductById(1L)).willReturn(Optional.of(myProducts.get(1)));

        mockMvc.perform(get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.model", is("Latitude 5590")))
                .andExpect(jsonPath("$.companyName", is("Dell")));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateNewProduct() throws Exception {


        ProductDto newProduct = new ProductDto();
        newProduct.setId(3L);
        newProduct.setModel("IPhone XIV");
        newProduct.setPrice(new BigDecimal("1400.34"));
        newProduct.setCompanyName("Apple");
        newProduct.setCategoryName("Phone");

        given(productService.createNewProduct(any(ProductDto.class))).willReturn(Optional.of(newProduct));

        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().string("Product has been successfully created!"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateWholeProductById() throws Exception {
        given(productService.updateWholeProductById(any(Long.class), any(ProductDto.class))).willReturn(
                Optional.of(update));

        given(productService.getProductById(any(Long.class))).willReturn(Optional.of(productFirst));

        mockMvc.perform(put("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productFirst))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(productService).updateWholeProductById(any(Long.class), any(ProductDto.class));
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void testDeleteProductById() throws Exception {

        given(productService.deleteProductById(any(Long.class))).willReturn(true);
        mockMvc.perform(delete("/products/1")
                        .with(csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService).deleteProductById(idArgumentCaptor.capture());

        assertEquals(1, idArgumentCaptor.getValue());
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void testUpdatePartOfProduct() throws Exception {

        Map<String, Object> shortTermMap = new HashMap<>();
        shortTermMap.put("companyName", "Lenovo");

        given(productService.getProductById(any(Long.class))).willReturn(Optional.of(productFirst));
        given(productService.updatePartOfProductById(any(Long.class), any(ProductDto.class))).willReturn(Optional.of(update));
        mockMvc.perform(patch("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortTermMap))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(productService).updatePartOfProductById(idArgumentCaptor.capture(), productArgumentCaptor.capture());

        assertEquals(1L, idArgumentCaptor.getValue());
        assertEquals(productArgumentCaptor.getValue().getCompanyName(), "Lenovo");
    }

}
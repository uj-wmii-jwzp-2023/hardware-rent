package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.data.dto.ProductDto;
import uj.wmii.jwzp.hardwarerent.services.interfaces.CategoryService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);


    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity getAllProducts() {
        List<Product> productsReturned;
        try {
            productsReturned = productService.getAllProducts();
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to get all products");
        return ResponseEntity.ok().body(productService.getProductDtoList(productsReturned));

    }

    @GetMapping("{id}")
    public ResponseEntity getProductById(@PathVariable(name = "id") Long id) {

        Optional productReturned;
        try{
            productReturned = productService.getProductById(id);
            if(productReturned.isEmpty()) {
                LOG.info("Failed to find product with id: '{}'",id);
                return ResponseEntity.status(404).body("Failed to find product with id: "+id);
            }
            LOG.info("Proceeded request to get category with id: '{}'",id);
            return ResponseEntity.ok().body(new ProductDto((Product) productReturned.get()));
        }catch (Exception e) {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error");
        }

    }

    @PostMapping
    public ResponseEntity createNewProduct(@RequestBody ProductDto productDto) {

        Product savedProduct;
        try {
            if(productService.getProductByModel(productDto.getModel()).isPresent()){
                LOG.info("Received request to add product with already existing model");
                return ResponseEntity.badRequest().body("product with such name already existed");
            }
            Optional category = categoryService.getCategoryByName(productDto.getCategoryName());
            if(category.isEmpty()){
                LOG.info("Received request to add product with non existed category");
                return ResponseEntity.badRequest().body("no category with such name");
            }

            Product productToSave = new Product(productDto);
            productToSave.setCategory(categoryService.getCategoryByName(productDto.getCategoryName()).get());
            savedProduct = productService.createNewProduct(productToSave);

            if (savedProduct == null){
                LOG.error("internal server error while creating new product");
                return ResponseEntity.internalServerError().body("Error while creating new product");
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to create new product");
        return ResponseEntity.created(URI.create("/products/" + savedProduct.getId()))
                .body("Product has been successfully created!");

    }

    @PutMapping("{id}")
    public ResponseEntity updateWholeProductById(@PathVariable("id") Long id, @RequestBody Product product) {
        Product updatedProduct;
        try {
            updatedProduct = productService.updateWholeProductById(id, product);
        }catch (Exception e)
        {
            if(e instanceof NoSuchElementException) {
                LOG.info("Failed to find product with id: '{}'",id);
                return ResponseEntity.status(404).body("Failed to find product with id: "+id);
            }
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to update product with id: "+id);
        return ResponseEntity.created(URI.create("/products/" + updatedProduct.getId()))
                .body("Product has been successfully changed!");
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") Long id) {

        Optional targetProduct;
        try {
            targetProduct = productService.getProductById(id);
            if(targetProduct.isEmpty()) {
                LOG.info("Failed to find product with id: '{}'",id);
                return ResponseEntity.status(404).body("Failed to find product with id: "+id);
            }else {
                productService.deleteProductById(id);
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to delete product with id: '{}'",id);
        return ResponseEntity.ok().body("deleted products with id: "+id);
    }

    @PatchMapping("{id}")
    public ResponseEntity updatePartOfProductById(@PathVariable("id") Long id, @RequestBody Product product) {
        Product updatedProduct;
        try {
            updatedProduct = productService.updatePartOfProductById(id, product);
        }catch (Exception e)
        {
            if(e instanceof NoSuchElementException) {
                LOG.info("Failed to find product with id: '{}'",id);
                return ResponseEntity.status(404).body("Failed to find product with id: "+id);
            }
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to update product with id: "+id);
        return ResponseEntity.created(URI.create("/products/" + updatedProduct.getId()))
                .body("Product has been successfully changed!");
    }


}

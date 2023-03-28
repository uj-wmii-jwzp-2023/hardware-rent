package uj.wmii.jwzp.hardwarerental.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.services.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{productId}")
    public Optional<Product> getProductById(@PathVariable("productId") Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public ResponseEntity addNewProduct(Product product) {

        Product savedProduct = productService.addNewProduct(product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/products" + savedProduct.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity deleteProductById(@PathVariable("productId") Long productId) {

        productService.deleteProductById(productId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{productId}")
    public ResponseEntity updateWholeProductById(@PathVariable("productId") Long productId, Product product) {

        productService.updateWholeProductById(productId, product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/products" + productId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PatchMapping("{productId}")
    public ResponseEntity updatePartOfProductById(@PathVariable("productId") Long productId, Product product) {

        productService.updatePartOfProductById(productId, product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/products/" + productId.toString());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = "productAvailability")
    public List<Product> returnProductByAvailability(@RequestParam Boolean productAvailability) {

        var getAvailableOrNotAvailableProducts = productService.returnProductsByAvailability(productAvailability);

        return getAvailableOrNotAvailableProducts;
    }
}

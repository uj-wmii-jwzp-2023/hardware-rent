package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.models.Product;
import uj.wmii.jwzp.hardwarerent.services.ProductService;

import java.net.URI;
import java.util.List;

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

    @GetMapping("{id}")
    public ResponseEntity getProductById(@PathVariable(name = "id") Long id) {
        var productReturned = productService.getProductById(id);
        if (productReturned.isEmpty())
            return ResponseEntity.status(404).body("Product with id: " + id + " does not exist!");
        return ResponseEntity.ok().body(productReturned);
    }

    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody Product product) {

        Product savedProduct = productService.createNewProduct(product);

        return ResponseEntity.created(URI.create("/products/" + savedProduct.getId()))
                .body("Product has been successfully created!");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateWholeProductById(@PathVariable("id") Long id, @RequestBody Product product) {
        productService.updateWholeProductById(id, product);

        return ResponseEntity.noContent().header("Location", "/products/" + id).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long id) {
        productService.deleteProductById(id);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<String> updatePartOfProductById(@PathVariable("id") Long id, @RequestBody Product product) {
        productService.updatePartOfProductById(id, product);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/products/" + id.toString());

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @GetMapping(params = "availability")
    public List<Product> getProductsByAvailability(@RequestParam Boolean availability) {
        return productService.getProductsByAvailability(availability);
    }
}

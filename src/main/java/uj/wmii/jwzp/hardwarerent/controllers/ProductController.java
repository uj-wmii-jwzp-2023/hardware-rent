package uj.wmii.jwzp.hardwarerent.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Product;
import uj.wmii.jwzp.hardwarerent.dtos.ProductDto;
import uj.wmii.jwzp.hardwarerent.mappers.ProductMapper;
import uj.wmii.jwzp.hardwarerent.services.interfaces.CategoryService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false)String companyName,
                                           @RequestParam(required = false)BigDecimal price,
                                           @RequestParam(required = false)Integer availableQuantity,
                                           @RequestParam(required = false)Integer overallQuantity,
                                           @RequestParam(required = false)String categoryName) {
        return productService.getAllProducts(companyName, price, availableQuantity, overallQuantity, categoryName);
    }

    @GetMapping("{id}")
    public ProductDto getProductById(@PathVariable(name = "id") Long id) {

        return productService.getProductById(id).orElseThrow(
                () -> new NotFoundException("Failed to find product with id: " + id));

    }

    @PostMapping
    public ResponseEntity createNewProduct(@Valid @RequestBody ProductDto productDto) {
        if (productService.getProductByModel(productDto.getModel()).isPresent())
            throw new AlreadyExistsException("Product with such name already exists!");
        Optional<ProductDto> savedProduct = productService.createNewProduct(productDto);
        if (savedProduct.isEmpty())
            throw new NotFoundException("No category with name: " + productDto.getCategoryName());

        return ResponseEntity.created(URI.create("/products/" + savedProduct.get().getId()))
                .body("Product has been successfully created!");

    }

    @PutMapping("{id}")
    public ResponseEntity updateWholeProductById(@PathVariable("id") Long id, @Valid @RequestBody ProductDto productDto) {
        if (productService.getProductById(id).isEmpty())
            throw new NotFoundException("Failed to find product with id: " + id);
        if (productService.getProductByModel(productDto.getModel()).isPresent())
            throw new AlreadyExistsException("Product with such name already exists!");
        if (productService.updateWholeProductById(id, productDto).isEmpty())
            throw new NotFoundException("Category cannot be changed!");

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") Long id) {

        if (!productService.deleteProductById(id)) {
            throw new NotFoundException("Failed to find product with id: " + id);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{id}")
    public ResponseEntity updatePartOfProductById(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
        if (productService.getProductById(id).isEmpty())
            throw new NotFoundException("Failed to find product with id: " + id);
        if (productService.getProductByModel(productDto.getModel()).isPresent())
            throw new AlreadyExistsException("Product with such name already exists!");
        if (productService.updatePartOfProductById(id, productDto).isEmpty())
            throw new NotFoundException("Category cannot be changed!");

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}

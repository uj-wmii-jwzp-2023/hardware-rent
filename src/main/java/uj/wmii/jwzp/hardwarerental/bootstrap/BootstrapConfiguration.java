package uj.wmii.jwzp.hardwarerental.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerental.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class BootstrapConfiguration implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public BootstrapConfiguration(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Category category_Tv = new Category();
        category_Tv.setCategoryName("TV");
        category_Tv = categoryRepository.save(category_Tv);

        Category category_Laptop = new Category();
        category_Laptop.setCategoryName("Laptop");
        category_Laptop = categoryRepository.save(category_Laptop);

        Product pr0 = new Product();
        pr0.setAvailable(true);
        pr0.setCompanyName("Samsung");
        pr0.setModel("tv 2000");
        pr0.setPrice(new BigDecimal("900"));
        pr0.setCategory(category_Tv);
        pr0 = productRepository.save(pr0);

        Product pr1 = new Product();
        pr1.setAvailable(false);
        pr1.setCompanyName("Dell");
        pr1.setModel("laptop 2000");
        pr1.setPrice(new BigDecimal("1000"));
        pr1.setCategory(category_Laptop);
        pr1 = productRepository.save(pr1);

        Product pr2 = new Product();
        pr2.setAvailable(false);
        pr2.setCompanyName("Acer");
        pr2.setModel("predator 500");
        pr2.setPrice(new BigDecimal("1200"));
        pr2.setCategory(category_Laptop);
        pr2 = productRepository.save(pr2);

        Set<Product> laptops = new HashSet<>();
        laptops.add(pr1);
        laptops.add(pr2);
        category_Laptop.setProducts(laptops);


    }
}

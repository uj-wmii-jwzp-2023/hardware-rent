package uj.wmii.jwzp.hardwarerental.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;
import uj.wmii.jwzp.hardwarerental.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerental.repositories.ProductRepository;

import java.math.BigDecimal;

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

        Category category = new Category();
        category.setCategoryName("TV");
        Category categorySaved = categoryRepository.save(category);

        Category laptop = new Category();
        laptop.setCategoryName("Laptop");
        Category laptopSaved = categoryRepository.save(laptop);

        Product pr = new Product();
        pr.setAvailable(true);
        pr.setCompanyName("Samsung");
        pr.setModel("tv 2000");
        pr.setPrice(new BigDecimal("2313"));
        Product prSaved = productRepository.save(pr);

        Product pr1 = new Product();
        pr1.setAvailable(false);
        pr1.setCompanyName("Dell");
        pr1.setModel("laptop 2000");
        pr1.setPrice(new BigDecimal("23134"));
        Product pr1Saved = productRepository.save(pr1);
    }
}

package uj.wmii.jwzp.hardwarerental.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import uj.wmii.jwzp.hardwarerental.data.Basket;
import uj.wmii.jwzp.hardwarerental.data.Category;
import uj.wmii.jwzp.hardwarerental.data.Product;

import uj.wmii.jwzp.hardwarerental.repositories.BasketRepository;
import uj.wmii.jwzp.hardwarerental.repositories.CategoryRepository;
import uj.wmii.jwzp.hardwarerental.repositories.ProductRepository;

import java.math.BigDecimal;

@Component
public class MyConfiguration implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final BasketRepository basketRepository;
    public MyConfiguration(CategoryRepository categoryRepository,
                           ProductRepository productRepository, BasketRepository basketRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        Category category = new Category();
        category.setCategoryName("TV");
        Category categorySaved = categoryRepository.save(category);

        Category laptop = new Category();
        laptop.setCategoryName("Laptop");
        Category laptopSaved = categoryRepository.save(laptop);

        Basket basket = new Basket();
        basket.setId(1L);
        Basket basketSaved = basketRepository.save(basket);

        Product pr = new Product();
        pr.setAvailable(true);
        pr.setCompanyName("Samsung");
        pr.setModel("tv 2000");
        pr.setCategory(categorySaved);
        pr.setPrice(new BigDecimal("2313"));
        pr.setBasket(basketSaved);
        Product prSaved = productRepository.save(pr);

        Product pr1 = new Product();
        pr1.setAvailable(false);
        pr1.setCompanyName("Dell");
        pr1.setModel("laptop 2000");
        pr1.setPrice(new BigDecimal("23134"));
        pr1.setBasket(basketSaved);
        Product pr1Saved = productRepository.save(pr1);


    }
}

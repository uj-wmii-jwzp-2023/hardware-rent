package uj.wmii.jwzp.hardwarerent.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uj.wmii.jwzp.hardwarerent.controllers.RegistrationController;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.repositories.*;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class BootstrapConfiguration implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final  OrderDetailsRepository orderDetailsRepository;
    private final RegistrationController registrationController;

    public BootstrapConfiguration(CategoryRepository categoryRepository,
                                  ProductRepository productRepository,
                                  UserRepository userRepository,
                                  OrdersRepository ordersRepository,
                                  OrderDetailsRepository orderDetailsRepository,
                                  RegistrationController registrationController) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.registrationController = registrationController;
    }


    @Override
    public void run(String... args) throws Exception {

        Category category_Tv = new Category();
        category_Tv.setCategoryName("TV");
        category_Tv = categoryRepository.save(category_Tv);

        Category category_Laptop = new Category();
        category_Laptop.setCategoryName("Laptop");
        category_Laptop = categoryRepository.save(category_Laptop);

        Product pr0 = new Product(
                "Samsung",
                "tv 2000",
                category_Tv,
                900,
                3);
        pr0 = productRepository.save(pr0);

        Product pr1 = new Product("Dell",
                "laptop 2000",
                category_Laptop,
                1000,
                5);
        pr1 = productRepository.save(pr1);

        Product pr2 = new Product(
                "Acer",
                "predator 500",
                category_Laptop,
                1200,
                10);
        pr2 = productRepository.save(pr2);

        MyUser user = new MyUser("admin","admin","test","test","test");
        registrationController.registerUser(user);
        //----
        Orders order = new Orders(user,new Date(),new Date());
        order = ordersRepository.save(order);

        OrderDetails orderDetails = new OrderDetails(pr0,1,"no description",order);
        orderDetailsRepository.save(orderDetails);
        //----
        order = new Orders(user,new Date(),new Date());
        order = ordersRepository.save(order);

        orderDetails = new OrderDetails(pr1,1,"no description",order);
        orderDetailsRepository.save(orderDetails);
        orderDetails = new OrderDetails(pr2,1,"no description",order);
        orderDetailsRepository.save(orderDetails);
        //----



    }
}

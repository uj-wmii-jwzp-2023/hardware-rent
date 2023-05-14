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
    private final RegistrationController registrationController;

    public BootstrapConfiguration(CategoryRepository categoryRepository,
                                  ProductRepository productRepository,
                                  UserRepository userRepository,
                                  OrdersRepository ordersRepository,
                                  RegistrationController registrationController) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
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

        Product pr0 = Product.builder()
                .companyName("Samsung")
                .model("tv2000")
                .category(category_Tv)
                .price(new BigDecimal(900))
                .availableQuantity(3)
                .overallQuantity(3)
                .build();
        pr0 = productRepository.save(pr0);

        Product pr1 = Product.builder()
                .companyName("Dell")
                .model("laptop 2000")
                .category(category_Laptop)
                .price(new BigDecimal(1000))
                .availableQuantity(5)
                .overallQuantity(5)
                .build();
        pr1 = productRepository.save(pr1);


        Product pr2 = Product.builder()
                .companyName("Acer")
                .model("predator 500")
                .category(category_Laptop)
                .price(new BigDecimal(1200))
                .availableQuantity(10)
                .overallQuantity(10)
                .build();
        pr2 = productRepository.save(pr2);


        Product pr3 = Product.builder()
                .companyName("Samsung")
                .model("new Samsung tv 1")
                .category(category_Tv)
                .price(new BigDecimal(341))
                .availableQuantity(2)
                .overallQuantity(3)
                .build();
        pr3 = productRepository.save(pr3);


        Product pr4 = Product.builder()
                .companyName("Samsung")
                .model("new samsung tv 2")
                .category(category_Tv)
                .price(new BigDecimal(704))
                .availableQuantity(1)
                .overallQuantity(8)
                .build();
        pr4 = productRepository.save(pr4);

        /*MyUser user = new MyUser("admin","admin","test","test","test");
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
*/

    }
}

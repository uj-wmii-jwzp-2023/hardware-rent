package uj.wmii.jwzp.hardwarerent.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.repositories.*;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class BootstrapConfiguration implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final ArchivedProductsRepository archivedProductsRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    public BootstrapConfiguration(CategoryRepository categoryRepository,
                                  ProductRepository productRepository,
                                  UserRepository userRepository,
                                  OrdersRepository ordersRepository,
                                  ArchivedProductsRepository archivedProductsRepository,
                                  OrderDetailsRepository orderDetailsRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
        this.archivedProductsRepository = archivedProductsRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        List<Category> categories = saveCategories();
        readProducts(categories);
        prepareOrdersForAdmin(userRepository.findByUsername("admin"));
        prepareOrdersForUser(userRepository.findByUsername("mladbago"));
    }

    public List<Category> saveCategories() {
        List<Category> categories = new ArrayList<>();
        Category categoryLaptop = new Category();
        categoryLaptop.setCategoryName("Laptop");
        categories.add(categoryRepository.save(categoryLaptop));

        Category categoryProcessor = new Category();
        categoryProcessor.setCategoryName("Processor");
        categories.add(categoryRepository.save(categoryProcessor));

        Category categoryPhone = new Category();
        categoryPhone.setCategoryName("Phone");
        categories.add(categoryRepository.save(categoryPhone));

        Category categoryEarPhones = new Category();
        categoryEarPhones.setCategoryName("EarPhones");
        categories.add(categoryRepository.save(categoryEarPhones));

        Category categoryTV = new Category();
        categoryTV.setCategoryName("TV");
        categories.add(categoryRepository.save(categoryTV));

        Category categoryRam = new Category();
        categoryRam.setCategoryName("RAM");
        categories.add(categoryRepository.save(categoryRam));

        Category categoryGPU = new Category();
        categoryGPU.setCategoryName("GPU");
        categories.add(categoryRepository.save(categoryGPU));

        return categories;
    }

    public void readProducts(List<Category> categories) {
        List<List<String>> records = new ArrayList<>();
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("csvdata/products.csv");
        try (InputStreamReader isr = new InputStreamReader(ioStream);
             BufferedReader br = new BufferedReader(isr);) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(", ");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.err.println("Error while reading file!");
        }

        int i = 0;
        for (var record: records) {
            Product newProduct = Product.builder()
                    .companyName(record.get(0))
                    .model(record.get(1))
                    .price(new BigDecimal(record.get(2)))
                    .build();
            if (i == 0 || i == 7 || i == 8 || i == 9)
                newProduct.setCategory(categories.get(0));
            if (i == 1 || i == 2)
                newProduct.setCategory(categories.get(1));
            if (i == 3 || i == 11 || i == 5)
                newProduct.setCategory(categories.get(2));
            if (i == 4)
                newProduct.setCategory(categories.get(3));
            if (i == 6 || i == 12)
                newProduct.setCategory(categories.get(4));
            if (i == 10)
                newProduct.setCategory(categories.get(5));
            if (i == 13 || i == 14)
                newProduct.setCategory(categories.get(6));

            productRepository.save(newProduct);
            archivedProductsRepository.save(new ArchivedProducts(newProduct));
            i ++;
        }
    }

    public void prepareOrdersForAdmin(MyUser user) {

        Order firstOrder = new Order();
        firstOrder.setOrderStatus(OrderStatus.CREATED);
        firstOrder.setUser(user);
        prepareDateTime(0, firstOrder);
        OrderDetails orderDetails = new OrderDetails(archivedProductsRepository.findById(1L).get(),
                null, firstOrder);
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        firstOrder.setOrderDetails(orderDetailsSet);
        firstOrder.setOverallCashSum();
        orderDetailsRepository.save(orderDetails);
        ordersRepository.save(firstOrder);


        Order secondOrder = new Order();
        secondOrder.setOrderStatus(OrderStatus.INITIALIZED);
        secondOrder.setUser(user);
        prepareDateTime(1, secondOrder);
        secondOrder.setOverallCashSum();
        ordersRepository.save(secondOrder);

        Order thirdOrder = new Order();
        thirdOrder.setOrderStatus(OrderStatus.INITIALIZED);
        thirdOrder.setUser(user);
        prepareDateTime(2, thirdOrder);
        thirdOrder.setOverallCashSum();
        ordersRepository.save(thirdOrder);

        Order fourthOrder = new Order();
        fourthOrder.setOrderStatus(OrderStatus.INITIALIZED);
        fourthOrder.setUser(user);
        prepareDateTime(3, fourthOrder);
        fourthOrder.setOverallCashSum();
        ordersRepository.save(fourthOrder);

        Order fifthOrder = new Order();
        fifthOrder.setOrderStatus(OrderStatus.INITIALIZED);
        fifthOrder.setUser(user);
        prepareDateTime(4, fifthOrder);
        fifthOrder.setOverallCashSum();
        ordersRepository.save(fifthOrder);


        Order sixthOrder = new Order();
        sixthOrder.setOrderStatus(OrderStatus.INITIALIZED);
        sixthOrder.setUser(user);
        prepareDateTime(5, sixthOrder);
        sixthOrder.setOverallCashSum();
        ordersRepository.save(sixthOrder);
    }

    public void prepareDateTime(int index, Order order) {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime futureDateTimeOrder;
        LocalDateTime futureDateTimeDue;
        if (index == 0) {
            LocalDate futureDate = currentDate.plusDays(13);
            LocalTime futureTime = LocalTime.of(13, 0);
            futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
            futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(10), futureTime);
            order.setOrderDate(futureDateTimeOrder);
            order.setDueDate(futureDateTimeDue);
        }
        if (index == 1) {
            LocalDate futureDate = currentDate.plusDays(25);
            LocalTime futureTime = LocalTime.of(17, 0);
            futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
            futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(6), futureTime);
            order.setOrderDate(futureDateTimeOrder);
            order.setDueDate(futureDateTimeDue);
        }
        if (index == 2) {
            LocalDate futureDate = currentDate.plusDays(40);
            LocalTime futureTime = LocalTime.of(14, 25);
            futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
            futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(3), futureTime.plusMinutes(35));
            order.setOrderDate(futureDateTimeOrder);
            order.setDueDate(futureDateTimeDue);
        }
        if (index == 3) {
            LocalDate futureDate = currentDate.plusDays(60);
            LocalTime futureTime = LocalTime.of(12, 10);
            futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
            futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(23), futureTime.plusMinutes(22));
            order.setOrderDate(futureDateTimeOrder);
            order.setDueDate(futureDateTimeDue);
        }
        if (index == 4) {
            LocalDate futureDate = currentDate.plusDays(90);
            LocalTime futureTime = LocalTime.of(17, 12);
            futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
            futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(26), futureTime.plusMinutes(44));
            order.setOrderDate(futureDateTimeOrder);
            order.setDueDate(futureDateTimeDue);
        }
        if (index == 5) {
            LocalDate futureDate = currentDate.plusDays(100);
            LocalTime futureTime = LocalTime.of(16, 54);
            futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
            futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(12), futureTime.plusMinutes(33));
            order.setOrderDate(futureDateTimeOrder);
            order.setDueDate(futureDateTimeDue);
        }

        ordersRepository.save(order);
    }

    public void prepareOrdersForUser(MyUser user) {

        Order firstOrder = new Order();
        firstOrder.setOrderStatus(OrderStatus.INITIALIZED);
        firstOrder.setUser(user);
        prepareDateTime(0, firstOrder);
        OrderDetails orderDetails = new OrderDetails(archivedProductsRepository.findById(1L).get(),
                null, firstOrder);
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        firstOrder.setOrderDetails(orderDetailsSet);
        firstOrder.setOverallCashSum();
        orderDetailsRepository.save(orderDetails);
        ordersRepository.save(firstOrder);

        Order secondOrder = new Order();
        secondOrder.setOrderStatus(OrderStatus.INITIALIZED);
        secondOrder.setUser(user);
        prepareDateTime(1, secondOrder);
        secondOrder.setOverallCashSum();
        ordersRepository.save(secondOrder);

        Order thirdOrder = new Order();
        thirdOrder.setOrderStatus(OrderStatus.INITIALIZED);
        thirdOrder.setUser(user);
        prepareDateTime(2, thirdOrder);
        thirdOrder.setOverallCashSum();
        ordersRepository.save(thirdOrder);

    }

}

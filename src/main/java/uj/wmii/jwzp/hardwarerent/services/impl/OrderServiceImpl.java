package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;
import uj.wmii.jwzp.hardwarerent.exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.exceptions.NoEnoughMoneyException;
import uj.wmii.jwzp.hardwarerent.exceptions.NotFoundException;
import uj.wmii.jwzp.hardwarerent.repositories.ArchivedProductsRepository;
import uj.wmii.jwzp.hardwarerent.repositories.OrderDetailsRepository;
import uj.wmii.jwzp.hardwarerent.repositories.OrdersRepository;
import uj.wmii.jwzp.hardwarerent.repositories.ProductRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;

    private final OrderDetailsService orderDetailsService;

    private final OrderDetailsRepository orderDetailsRepository;

    private final ProductRepository productRepository;
    private final ArchivedProductsRepository archivedProductsRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository, OrderDetailsService orderDetailsService,
                            OrderDetailsRepository orderDetailsRepository, ProductRepository productRepository,
                            ArchivedProductsRepository archivedProductsRepository) {
        this.ordersRepository = ordersRepository;
        this.orderDetailsService = orderDetailsService;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productRepository = productRepository;
        this.archivedProductsRepository = archivedProductsRepository;
    }

    @Override
    public Optional<Order> createNewOrder(OrderDto orderDto,
                                          MyUser user,
                                          Clock clock) {
        LocalDateTime todaysDateTime = LocalDateTime
                .ofInstant(clock.instant(), ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate todaysDate = todaysDateTime.toLocalDate();
        LocalDate orderDateFormated =  LocalDate.parse(orderDto.getOrderDate(), formatter);
        LocalDate dueDateFormated  = LocalDate.parse(orderDto.getDueDate(), formatter);

        if (checkOrderDatesOverlapping(orderDto, orderDateFormated, dueDateFormated))
            throw new InvalidDatesException("Dates overlapping between orders. Please change dates!");
        if (todaysDate.compareTo(orderDateFormated) > 0)
            throw new InvalidDatesException("orderDate should be greater or equals to today date");
        if (orderDateFormated.compareTo(dueDateFormated) > 0)
            throw new InvalidDatesException("orderDate shoulf be smaller than dueDate!");
        Order orderToAdd = new Order(user, orderDateFormated, dueDateFormated, new HashSet<>());

        var orderDetailsToAdd =
                orderDetailsService.createOrderDetailsListFromOrderDetailsDtoList(orderDto.getOrderDetails(),orderToAdd);

        orderToAdd.setOrderDetails(orderDetailsToAdd);
        orderToAdd.setOverallCashSum();

        var orderAdded = ordersRepository.save(orderToAdd);

        return Optional.of(orderAdded);
    }


    @Override
    public List<Order> getAllOrdersForUser(MyUser user) {
        return ordersRepository.findAllByUser(user);
    }

    @Override
    public boolean deleteOrderDetailFromOrder(MyUser user, Long orderId, Long orderDetailId) {
        Optional<Order> orderReturned = ordersRepository.findByUserAndId(user, orderId);
        if (orderReturned.isEmpty())
            throw new NotFoundException("Failed to find order with id: " + orderId);

        Optional<OrderDetails> orderDetailSaved = orderDetailsRepository.findById(orderDetailId);
        if (orderDetailSaved.isEmpty())
            throw new NotFoundException("Failed to find orderDetail with id: " + orderDetailId
                    + "for order with id: " + orderId);
        Order myOrder = orderReturned.get();
        myOrder.getOrderDetails().remove(orderDetailSaved.get());
        myOrder.setOverallCashSum();
        orderDetailsRepository.deleteById(orderDetailId);

        return true;
    }

    @Override
    public void addOrderDetailsToOrder(MyUser user, OrderDetailsDto orderDetailsDto) {
        Optional<Order> orderReturned = ordersRepository.findByUserAndId(user, orderDetailsDto.getOrderId());
        if (orderReturned.isEmpty())
            throw new NotFoundException("Failed to find order with id: " + orderDetailsDto.getOrderId());
        Optional<Product> productReturned = productRepository.findById(orderDetailsDto.getProductId());
        if (productReturned.isEmpty())
            throw new NotFoundException("Failed to find product with id: " + orderDetailsDto.getOrderId());
        if (checkOrderDatesOverlappingForOneProduct(orderReturned.get().getOrderDate(), orderReturned.get().getDueDate(),
                orderDetailsDto.getProductId()))
            throw new InvalidDatesException("Overlapping dates for product!");

        Order order = orderReturned.get();
        ArchivedProducts productToAdd = archivedProductsRepository.findById(orderDetailsDto.getProductId()).get();

        OrderDetails orderDetails = new OrderDetails(productToAdd, orderDetailsDto.getDescription(), order);
        order.getOrderDetails().add(orderDetails);
        order.setOverallCashSum();
        orderDetailsRepository.save(orderDetails);
        ordersRepository.save(order);

    }

    @Override
    public void payForProduct(MyUser user, Long id, String orderStatus) {
        System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");
        System.out.println("SKDAJKASD");


        Optional<Order> orderReturned = ordersRepository.findByUserAndId(user, id);
        if (orderReturned.isEmpty())
            throw new NotFoundException("Failed to find order with id: " + id);

        var productIds = orderReturned.get().getOrderDetails()
                .stream().map(x -> x.getArchivedProducts().getProductId())
                .distinct().toList();

        System.out.println(orderStatus);
        if (OrderStatus.valueOf(orderStatus) == OrderStatus.CREATED) {
            if (checkOrderDatesOverlappingForList(orderReturned.get().getOrderDate(), orderReturned.get().getDueDate(),
                    productIds))
                throw new InvalidDatesException("Dates overlapping by another order!");
            System.out.println(user.getCash());
            if (user.getCash().compareTo(orderReturned.get().getOverallCashSum()) < 0)
                throw new NoEnoughMoneyException("You don't have enough money in your wallet!");
        }

        orderReturned.get().setOrderStatus(OrderStatus.valueOf(orderStatus));
        ordersRepository.save(orderReturned.get());
        user.setCash(user.getCash().subtract(orderReturned.get().getOverallCashSum()));
    }


    @Override
    public Optional<Order> changeOrderStatus(Long id, OrderStatus orderStatus) {
        Optional<Order> savedOrder = ordersRepository.findById(id);
        if (savedOrder.isEmpty())
            return Optional.empty();
        Order existing = savedOrder.get();
        existing.setOrderStatus(orderStatus);
        return Optional.of(ordersRepository.save(existing));

    }

    @Override
    public List<Order> getAllOrders() {
        return ordersRepository.findAll();
    }
    @Override
    public Optional<Order> getOrderById(Long id) {
        return ordersRepository.findById(id);
    }
    public boolean checkOrderDatesOverlapping(OrderDto orderDto,
                                              LocalDate orderDateFormated,
                                              LocalDate dueDateFormated) {
        List<Order> orders = ordersRepository.findAll();
        var productsInOrder = orderDto.getOrderDetails().stream()
                .map(OrderDetailsDto::getProductId)
                .distinct()
                .toList();
        var targetProducts = findTargetProducts(orders, orderDateFormated, dueDateFormated);
        var sameElements = productsInOrder.stream()
                .filter(targetProducts::contains).toList();
        System.out.println(targetProducts);
        return sameElements.size() > 0;
    }

    public boolean checkOrderDatesOverlappingForOneProduct(LocalDate orderDateFormated,
                                                           LocalDate dueDateFormated,
                                                           Long productId) {
        List<Order> orders = ordersRepository.findAll();
        var targetProducts = findTargetProducts(orders, orderDateFormated, dueDateFormated);
        System.out.println(targetProducts);
        return targetProducts.contains(productId);
    }
    public List<Long> findTargetProducts(List<Order> orders,
                                         LocalDate orderDateFormated,
                                         LocalDate dueDateFormated) {
        return orders.stream()
                .filter(x -> orderDateFormated.compareTo(x.getDueDate()) < 0
                        && dueDateFormated.compareTo(x.getOrderDate()) > 0)
                .filter(x -> x.getOrderStatus() == OrderStatus.CREATED)
                .map(Order::getOrderDetails)
                .flatMap(Collection::stream)
                .map(OrderDetails::getArchivedProducts)
                .map(ArchivedProducts::getProductId)
                .distinct()
                .toList();
    }

    public boolean checkOrderDatesOverlappingForList(LocalDate orderDateFormated,
                                                     LocalDate dueDateFormated,
                                                     List<Long> products) {
        List<Order> orders = ordersRepository.findAll();
        var targetProducts = findTargetProducts(orders, orderDateFormated, dueDateFormated);
        var sameElements = products.stream()
                .filter(targetProducts::contains).toList();

        return sameElements.size() > 0;
    }

}

package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;
import uj.wmii.jwzp.hardwarerent.exceptions.*;
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
        if (hasDuplicates(orderDto))
            throw new ProductAlreadyInOrderException("You cannot create an order with product duplicates!");
        Order orderToAdd = new Order(user, orderDateFormated, dueDateFormated, new HashSet<>());

        var orderDetailsToAdd =
                orderDetailsService.createOrderDetailsListFromOrderDetailsDtoList(orderDto.getOrderDetails(),orderToAdd);

        orderToAdd.setOrderDetails(orderDetailsToAdd);
        orderToAdd.setOverallCashSum();

        var orderAdded = ordersRepository.save(orderToAdd);

        return Optional.of(orderAdded);
    }


    @Override
    public List<Order> getAllOrdersForUser(MyUser user, String orderStatus) {
        if (!StringUtils.hasText(orderStatus)) {
            return ordersRepository.findAllByUser(user);
        }
        OrderStatus makeFromString = OrderStatus.valueOf(orderStatus);
        if (makeFromString != OrderStatus.CREATED && makeFromString != OrderStatus.INITIALIZED
                && makeFromString != OrderStatus.CANCELED && makeFromString != OrderStatus.FINISHED)
            throw new OrderStatusNotValidException("Only possible Enum values are INITIALIZED, CREATED, FINISHED and CANCELED!");

        return ordersRepository.findAllByOrderStatusAndUser(makeFromString, user);
    }

    @Override
    public void deleteOrderDetailFromOrder(MyUser user, Long orderId, Long orderDetailId) {
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
        if (hasDuplicatesForOne(orderDetailsDto, order))
            throw new ProductAlreadyInOrderException("Product cannot be added to the order, product with the same id is present!");
        ArchivedProducts productToAdd = archivedProductsRepository.findById(orderDetailsDto.getProductId()).get();

        OrderDetails orderDetails = new OrderDetails(productToAdd, orderDetailsDto.getDescription(), order);
        order.getOrderDetails().add(orderDetails);
        order.setOverallCashSum();
        orderDetailsRepository.save(orderDetails);
        ordersRepository.save(order);

    }

    @Override
    public void payForProduct(MyUser user, Long id, String orderStatus) {

        Optional<Order> orderReturned = ordersRepository.findByUserAndId(user, id);
        if (orderReturned.isEmpty())
            throw new NotFoundException("Failed to find order with id: " + id);

        var productIds = orderReturned.get().getOrderDetails()
                .stream().map(x -> x.getArchivedProducts().getProductId())
                .distinct().toList();

        System.out.println(orderStatus);
        if (OrderStatus.valueOf(orderStatus.toUpperCase()) != OrderStatus.CREATED) {
            throw new ChangeOrderStatusNotApplicableException("You can only change to CREATED and pay for product");
        }
        if (OrderStatus.valueOf(orderStatus.toUpperCase()) == OrderStatus.CREATED) {
            if (checkOrderDatesOverlappingForList(orderReturned.get().getOrderDate(), orderReturned.get().getDueDate(),
                    productIds))
                throw new InvalidDatesException("Dates overlapping by another order!");
            if (user.getCash().compareTo(orderReturned.get().getOverallCashSum()) < 0)
                throw new NoEnoughMoneyException("You don't have enough money in your wallet!");
        }

        orderReturned.get().setOrderStatus(OrderStatus.valueOf(orderStatus));
        ordersRepository.save(orderReturned.get());
        user.setCash(user.getCash().subtract(orderReturned.get().getOverallCashSum()));
    }

    @Override
    public Optional<Order> getOrderFromAllById(Long id) {
        return ordersRepository.findById(id);
    }


    @Override
    public Optional<Order> changeOrderStatus(Long id, String orderStatus) {
        Optional<Order> savedOrder = ordersRepository.findById(id);
        if (savedOrder.isEmpty())
            return Optional.empty();
        Order existing = savedOrder.get();
        OrderStatus makeFromString = OrderStatus.valueOf(orderStatus.toUpperCase());
        if (makeFromString == OrderStatus.INITIALIZED || makeFromString == OrderStatus.CREATED) {
            throw new ChangeOrderStatusNotApplicableException("Cannot change order status to " + orderStatus.toUpperCase() +
                    ". You can change only to CANCELED or FINISHED!");
        }
        existing.setOrderStatus(makeFromString);

        return Optional.of(ordersRepository.save(existing));

    }

    @Override
    public List<Order> getAllOrders(String orderStatus) {
        if (!StringUtils.hasText(orderStatus)) {
            return ordersRepository.findAll();
        }
        OrderStatus makeFromString = OrderStatus.valueOf(orderStatus);
        if (makeFromString != OrderStatus.CREATED && makeFromString != OrderStatus.INITIALIZED
                && makeFromString != OrderStatus.CANCELED && makeFromString != OrderStatus.FINISHED)
            throw new OrderStatusNotValidException("Only possible Enum values are INITIALIZED, CREATED, FINISHED and CANCELED!");

        return ordersRepository.findAllByOrderStatus(makeFromString);
    }
    @Override
    public Optional<Order> getOrderById(MyUser user, Long id) {
        return ordersRepository.findByUserAndId(user, id);
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

    public boolean hasDuplicates(OrderDto orderDto) {
        var listToCheck = orderDto.getOrderDetails().stream()
                .map(OrderDetailsDto::getProductId).toList();

        Set<Long> elements = new HashSet<>();
        List<Long> duplicates = listToCheck.stream()
                .filter(x -> !elements.add(x))
                .toList();

        return duplicates.size() > 0;
    }

    public boolean hasDuplicatesForOne(OrderDetailsDto orderDetailsDto, Order order) {
        var listToCheck = order.getOrderDetails().stream()
                .map(x -> x.getArchivedProducts().getProductId()).toList();
        Long element = orderDetailsDto.getProductId();
        var duplicates = listToCheck.stream().filter(x -> x.compareTo(element) == 0).toList();

        return duplicates.size() > 0;
    }
}

package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.ProductNotFoundException;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;
    private final MyUserDetailsService myUserDetailsService;
    private final OrderDetailsService orderDetailsService;
    private final Clock clock;

    public OrdersController(OrderService orderService,
                            UserRepository userRepository,
                            MyUserDetailsService myUserDetailsService,
                            OrderDetailsService orderDetailsService,
                            Clock clock) {
        this.orderService = orderService;
        this.myUserDetailsService = myUserDetailsService;
        this.orderDetailsService = orderDetailsService;
        this.clock = clock;
    }
    @GetMapping
    public ResponseEntity getAllOrders() {

        List<Order> orderReturned;
        try {
            orderReturned = orderService.getAllOrders();

        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to get all orders");
        return ResponseEntity.ok().body(orderReturned);
    }
    @PostMapping
    public ResponseEntity createNewOrder(@RequestBody OrderDto orderDto, Authentication authentication)
    {

        try {
            LocalDateTime todaysDateTime = LocalDateTime
                    .ofInstant(clock.instant(), ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate todaysDate = todaysDateTime.toLocalDate();
            LocalDate orderDateFormated =  LocalDate.parse(orderDto.getOrderDate(), formatter);
            LocalDate dueDateFormated  = LocalDate.parse(orderDto.getDueDate(), formatter);

            //check order date
            if (checkOrderDatesOverlapping(orderDto, orderDateFormated, dueDateFormated))
                throw new InvalidDatesException("Dates overlapping between orders. Please change dates!");
            if (todaysDate.compareTo(orderDateFormated) > 0)
                throw new InvalidDatesException("orderDate should be greater or equals to today date");

            //user check
            MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());
            if(user == null) {
                LOG.error("cant get username from authentication or user not exist");
                return ResponseEntity.badRequest().body("Error while searching user form authentication data");}


            //create order
            Order orderToAdd = new Order(user, orderDateFormated, dueDateFormated, new HashSet<>());
            //create order details and set order
            var orderDetailsToAdd = orderDetailsService
                    .createOrderDetailsListFromOrderDetailsDtoList(orderDto.getOrderDetails(),orderToAdd);


            //set orderDetails to order
            orderToAdd.setOrderDetails(orderDetailsToAdd);
            orderToAdd.setOverallCashSum();
            if (orderToAdd.getOverallCashSum().compareTo(user.getCash()) > 0) {
                return ResponseEntity.badRequest().body("Invalid transaction. Not enough money!");
            }
            user.setCash(user.getCash().subtract(orderToAdd.getOverallCashSum()));

            //saving order to repository
            var orderAdded = orderService.createNewOrder(orderToAdd);
            if(orderAdded == null){
                LOG.error("cant save order");
                return ResponseEntity.internalServerError().body("Error while saving order");}

            //success
            LOG.info("Proceeded request to create new order");
            return ResponseEntity.created(URI.create("/orders/" + orderAdded.getId()))
                    .body("Order has been successfully created!");
        }catch (Exception e)
        {
            if(e instanceof ParseException) {
                LOG.info("Error while creating new order: illegal dates:" + e.getMessage());
                return ResponseEntity.badRequest().body("illegal dates"); // return error response
            }
            if(e instanceof IllegalArgumentException){
                LOG.info("Error while creating new order: illegal dates format:" + e.getMessage());
                return ResponseEntity.badRequest().body("illegal dates format"); // return error response
            }
            if(e instanceof InvalidDatesException){
                LOG.info("Error while creating new order: " + e.getMessage());
                return ResponseEntity.badRequest().body("Error while creating new order: " + e.getMessage()); // return error response
            }
            if(e instanceof ProductNotFoundException){
                LOG.info("Error while creating new order: " + e.getMessage());
                return ResponseEntity.badRequest().body("Error while creating new order: " + e.getMessage()); // return error response
            }
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }


    }
    @GetMapping("{id}")
    public ResponseEntity getOrderById(@PathVariable Long id)
    {
        Optional orderReturned;
        try{
            orderReturned = orderService.getOrderById(id);
            if(orderReturned.isEmpty()) {
                LOG.info("Failed to find order with id: '{}'",id);
                return ResponseEntity.status(404).body("Failed to find order with id: "+id);
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to get order with id: '{}'",id);
        return ResponseEntity.ok().body(orderReturned);
    }

    @PatchMapping("{id}")
    public ResponseEntity changeOrderStatus(@PathVariable("id") Long id, OrderStatus orderStatus) {
        if (orderService.changeOrderStatus(id, orderStatus).isEmpty())
            throw new NotFoundException("Failed to find order with id: " + id);

        return ResponseEntity.ok().body("Successfully changed");
    }

    public boolean checkOrderDatesOverlapping(OrderDto orderDto,
                                              LocalDate orderDateFormated,
                                              LocalDate dueDateFormated) {
        List<Order> orders = orderService.getAllOrders();
        var productsInOrder = orderDto.getOrderDetails().stream()
                .map(OrderDetailsDto::getProductId)
                .distinct()
                .toList();
        var targetProducts = orders.stream()
                .filter(x -> orderDateFormated.compareTo(x.getDueDate()) < 0
                        && dueDateFormated.compareTo(x.getOrderDate()) > 0)
                .filter(x -> x.getOrderStatus() == OrderStatus.CREATED)
                .map(Order::getOrderDetails)
                .flatMap(Collection::stream)
                .map(OrderDetails::getArchivedProducts)
                .map(ArchivedProducts::getProductId)
                .distinct()
                .toList();
        var sameElements = productsInOrder.stream()
                .filter(targetProducts::contains).toList();

        return sameElements.size() > 0;
    }

}

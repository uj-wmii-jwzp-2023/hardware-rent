package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.ProductNotFoundException;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;
    private final MyUserDetailsService myUserDetailsService;
    private final OrderDetailsService orderDetailsService;
    private final Clock clock;

    public OrdersController(OrderService orderService, UserRepository userRepository, MyUserDetailsService myUserDetailsService, OrderDetailsService orderDetailsService, Clock clock) {
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
            Date todaysDate =  Date.from(clock.instant());
            Date orderDateFormated =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(orderDto.getOrderDate());
            Date dueDateFormated  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(orderDto.getDueDate());

            //check order date
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
            if(e instanceof InvalidDatesException){ // custom exception
                LOG.info("Error while creating new order: " + e.getMessage());
                return ResponseEntity.badRequest().body("Error while creating new order: " + e.getMessage()); // return error response
            }
            if(e instanceof ProductNotFoundException){ // custom exception
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
}

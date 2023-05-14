package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDto;
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
            Date orderDateFormated =  Date.from(clock.instant());
            Date dueDateFormated  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(orderDto.getDueDate());
            //user check
            MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());
            if(user == null) {
                LOG.error("internal server error while creating new order (find authenticated user)");
                return ResponseEntity.internalServerError().body("Error while creating new order");}
            //create order
            Order orderToAdd = new Order(user, orderDateFormated, dueDateFormated, new HashSet<>());
            if (orderToAdd == null){
                LOG.error("internal server error while creating new order");
                return ResponseEntity.internalServerError().body("Error while creating new order");}
            //create order details and add to order
            var orderDetailsToAdd = orderDetailsService.createOrderDetailsListFromOrderDetailsDtoListWithoutOrder(orderDto.getOrderDetails());
            var orderDetailsAdded = orderDetailsService.createNewOrderDetails(orderDetailsToAdd);
            for (var orderDetailToAdd: orderDetailsToAdd) {
                orderDetailToAdd.setOrder(orderToAdd);}
            orderToAdd.setOrderDetails(orderDetailsToAdd);
            var orderAdded = orderService.createNewOrder(orderToAdd);
            orderDetailsAdded = orderDetailsService.createNewOrderDetails(orderDetailsToAdd);
            //success
            LOG.info("Proceeded request to create new order");
            return ResponseEntity.created(URI.create("/orders/" + orderAdded.getId()))
                    .body("Order has been successfully created!");
        }catch (Exception e)
        {
            if(e instanceof ParseException) {
                LOG.info("Error while creating new order: " + e.getMessage());
                return ResponseEntity.badRequest().body("illegal dates"); // return error response
            }
            if(e instanceof IllegalArgumentException){
                LOG.info("Error while creating new order: " + e.getMessage());
                return ResponseEntity.badRequest().body("illegal dates format"); // return error response
            }
            if(e instanceof InvalidDatesException){
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

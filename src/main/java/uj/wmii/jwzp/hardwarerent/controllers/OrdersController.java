package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;
    private final MyUserDetailsService myUserDetailsService;

    public OrdersController(OrderService orderService, UserRepository userRepository, MyUserDetailsService myUserDetailsService) {
        this.orderService = orderService;
        this.myUserDetailsService = myUserDetailsService;
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
    public ResponseEntity createNewOrder(@RequestParam String orderDate,@RequestParam String dueDate, Authentication authentication)
    {

        try {
            Date orderDateFormated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(orderDate);
            Date dueDateFormated  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dueDate);
            MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());
            if(user == null) {
                LOG.error("internal server error while creating new order (find authenticated user)");
                return ResponseEntity.internalServerError().body("Error while creating new order");
            }
            Order orderAdded = orderService.createNewOrder(user, orderDateFormated, dueDateFormated);
            if (orderAdded == null){
                LOG.error("internal server error while creating new order");
                return ResponseEntity.internalServerError().body("Error while creating new order");
            }
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

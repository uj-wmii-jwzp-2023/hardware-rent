package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.*;

import uj.wmii.jwzp.hardwarerent.exceptions.NotExistingUserException;

import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;

import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;

import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.net.URI;
import java.time.Clock;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;
    private final MyUserDetailsService myUserDetailsService;
    private final Clock clock;

    public OrdersController(OrderService orderService,
                            UserRepository userRepository,
                            MyUserDetailsService myUserDetailsService,
                            Clock clock) {
        this.orderService = orderService;
        this.myUserDetailsService = myUserDetailsService;
        this.clock = clock;
    }
    @GetMapping
    public List<Order> getAllOrdersForUser(Authentication authentication) {

        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        return orderService.getAllOrdersForUser(user);

    }

    @GetMapping("/all")
    public List<Order> getAllOrders(Authentication authentication) {
        return orderService.getAllOrders();
    }
    @PostMapping
    public ResponseEntity createNewOrder(@RequestBody OrderDto orderDto, Authentication authentication)
    {
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");
        Optional<Order> orderOptional = orderService.createNewOrder(orderDto, user, clock);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.internalServerError().body("Error occured while creating order!");
        }
        Order orderAdded = orderOptional.get();
        return ResponseEntity.created(URI.create("/orders/" + orderAdded.getId()))
                .body("Order has been successfully created!");
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


    @DeleteMapping("{orderId}/{orderDetailId}")
    public ResponseEntity deleteOrderDetailFromOrder(@PathVariable("orderId") Long orderId,
                                                 @PathVariable("orderDetailId") Long orderDetailId,
                                                 Authentication authentication) {
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        orderService.deleteOrderDetailFromOrder(user, orderId, orderDetailId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/orderDetail")
    public ResponseEntity addOrderDetailToOrder(@RequestBody OrderDetailsDto orderDetailsDto,
                                                Authentication authentication) {
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        orderService.addOrderDetailsToOrder(user, orderDetailsDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping({"{id}"})
    public ResponseEntity payForOrder(@PathVariable("id") Long id,
                                      @RequestParam String orderStatus,
                                      Authentication authentication) {
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        orderService.payForProduct(user, id ,orderStatus);

        return ResponseEntity.ok().body("Order status changed to " + orderStatus);
    }
}

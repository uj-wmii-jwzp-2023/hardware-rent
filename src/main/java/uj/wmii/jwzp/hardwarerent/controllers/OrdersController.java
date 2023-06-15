package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.*;

import uj.wmii.jwzp.hardwarerent.exceptions.NotExistingUserException;

import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;

import uj.wmii.jwzp.hardwarerent.exceptions.NotFoundException;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;
import uj.wmii.jwzp.hardwarerent.services.impl.MyUserDetailsService;

import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.net.URI;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;
    private final MyUserDetailsService myUserDetailsService;

    public OrdersController(OrderService orderService,
                            UserRepository userRepository,
                            MyUserDetailsService myUserDetailsService) {
        this.orderService = orderService;
        this.myUserDetailsService = myUserDetailsService;
    }
    @GetMapping("/me")
    public List<Order> getAllOrdersForUser(@RequestParam(required = false) String orderStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        return orderService.getAllOrdersForUser(user, orderStatus);

    }

    @GetMapping("/all")
    public List<Order> getAllOrders(@RequestParam(required = false) String orderStatus) {
        return orderService.getAllOrders(orderStatus);
    }
    @PostMapping
    public ResponseEntity createNewOrder(@RequestBody OrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        Optional<Order> orderOptional = orderService.createNewOrder(orderDto, user);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.internalServerError().body("Error occured while creating order!");
        }
        Order orderAdded = orderOptional.get();
        return ResponseEntity.created(URI.create("/orders/" + orderAdded.getId()))
                .body("Order has been successfully created!");
    }
    @GetMapping("/me/{id}")
    public Order getOrderById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw  new NotExistingUserException("Please log in to have access to orders!");

        Optional<Order> orderReturned = orderService.getOrderById(user, id);
        if (orderReturned.isEmpty())
            throw new NotFoundException("Failed to find order with id: " + id);

        return orderReturned.get();
    }

    @GetMapping("/all/{id}")
    public Order getOrderByIdForAll(@PathVariable("id") Long id) {
        Optional<Order> orderReturned = orderService.getOrderFromAllById(id);
        if (orderReturned.isEmpty())
            throw new NotFoundException("Failed to find order with id: " + id);

        return orderReturned.get();
    }


    @DeleteMapping("{orderId}/{orderDetailId}")
    public ResponseEntity deleteOrderDetailFromOrder(@PathVariable("orderId") Long orderId,
                                                 @PathVariable("orderDetailId") Long orderDetailId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        orderService.deleteOrderDetailFromOrder(user, orderId, orderDetailId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/orderDetail")
    public ResponseEntity addOrderDetailToOrder(@RequestBody OrderDetailsDto orderDetailsDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        orderService.addOrderDetailsToOrder(user, orderDetailsDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping({"/me/{id}"})
    public ResponseEntity payForOrder(@PathVariable("id") Long id,
                                      @RequestParam String orderStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser user = myUserDetailsService.loadUserByUsername(authentication.getName());

        if (user == null)
            throw new NotExistingUserException("Please log in to have access to orders!");

        orderService.payForProduct(user, id ,orderStatus);

        return ResponseEntity.ok().body("Order status changed to " + orderStatus);
    }

    @PatchMapping("all/{id}")
    public ResponseEntity changeOrderStatus(@PathVariable("id") Long id,
                                            @RequestParam String orderStatus) {
        orderService.changeOrderStatus(id, orderStatus);

        return ResponseEntity.ok("Order status changed to " + orderStatus);
    }

}

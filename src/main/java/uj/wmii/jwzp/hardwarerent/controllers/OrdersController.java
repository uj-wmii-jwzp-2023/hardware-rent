package uj.wmii.jwzp.hardwarerent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
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
    public ResponseEntity createNewOrder(Order order)
    {
        Order orderAdded;
        try {
            orderAdded = orderService.createNewOrder(order);
            if (orderAdded == null){
                LOG.error("internal server error while creating new order");
                return ResponseEntity.internalServerError().body("Error while creating new order");
            }
        }catch (Exception e)
        {
            LOG.error("Internal error: " + e.getMessage());
            return ResponseEntity.internalServerError().body("internal server error"); // return error response
        }
        LOG.info("Proceeded request to create new order");
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
}

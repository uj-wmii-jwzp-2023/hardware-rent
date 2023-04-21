package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.data.Orders;
import uj.wmii.jwzp.hardwarerent.services.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public ResponseEntity getAllOrders() {
        var ordersReturned = orderService.getAllOrders();;
        if (ordersReturned == null)
            return ResponseEntity.status(404).body("Error while getting all orders");
        else
            return ResponseEntity.ok().body(ordersReturned);
    }
    @PostMapping
    public ResponseEntity addNewOrder(Orders order)
    {
        var orderAdded = orderService.addNewOrder(order);
        if (orderAdded == null)
            return ResponseEntity.status(404).body("Error while adding new order");
        else
            return ResponseEntity.ok().body(orderAdded);
    }
    @GetMapping("{id}")
    public ResponseEntity getOrderById(Long id)
    {
        var orderReturned = orderService.getOrderById(id);
        if (orderReturned.isEmpty())
            return ResponseEntity.status(404).body("Order with id: " + id + " does not exist!");
        else
            return ResponseEntity.ok().body(orderReturned);

    }
}

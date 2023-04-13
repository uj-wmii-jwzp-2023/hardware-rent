package uj.wmii.jwzp.hardwarerent.controllers;

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
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }
    @PostMapping
    public Orders addNewOrder(Orders order)
    {
        return orderService.addNewOrder(order);
    }
    @GetMapping("{id}")
    public Optional<Orders> getOrderById(Long id)
    {
        return orderService.getOrderById(id);
    }
}

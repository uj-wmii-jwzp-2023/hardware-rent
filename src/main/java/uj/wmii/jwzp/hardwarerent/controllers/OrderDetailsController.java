package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;

@RestController
public class OrderDetailsController {
    private final OrderDetailsService orderDetailsService;

    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

}

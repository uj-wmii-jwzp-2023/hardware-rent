package uj.wmii.jwzp.hardwarerental.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uj.wmii.jwzp.hardwarerental.data.Basket;
import uj.wmii.jwzp.hardwarerental.services.BasketService;

import java.util.Optional;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public Optional<Basket> getBasket() {
        return basketService.getProductsFromBasket();
    }

}

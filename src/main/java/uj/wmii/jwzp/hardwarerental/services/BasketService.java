package uj.wmii.jwzp.hardwarerental.services;

import uj.wmii.jwzp.hardwarerental.data.Basket;

import java.util.Optional;

public interface BasketService {

    Optional<Basket> getProductsFromBasket();
}

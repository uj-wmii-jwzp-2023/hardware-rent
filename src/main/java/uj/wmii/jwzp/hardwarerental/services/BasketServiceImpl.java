package uj.wmii.jwzp.hardwarerental.services;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerental.data.Basket;
import uj.wmii.jwzp.hardwarerental.repositories.BasketRepository;

import java.util.Optional;


@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;

    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public Optional<Basket> getProductsFromBasket() {
        long numberOfBaskets = basketRepository.lastBasket();
        return basketRepository.findById(numberOfBaskets);
    }
}

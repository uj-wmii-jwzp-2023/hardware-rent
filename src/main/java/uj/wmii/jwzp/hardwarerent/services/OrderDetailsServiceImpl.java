package uj.wmii.jwzp.hardwarerent.services;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.repositories.OrderDetailsRepository;
@Service
public class OrderDetailsServiceImpl implements OrderDetailsService{
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

}

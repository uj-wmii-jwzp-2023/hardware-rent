package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.repositories.OrdersRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Order createNewOrder(Order order) {
        return ordersRepository.save(order);
    }

    @Override
    public Order changeOrderInfo(Order order) {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return ordersRepository.findAll();
    }
    @Override
    public Optional<Order> getOrderById(Long id) {
        return ordersRepository.findById(id);
    }
}

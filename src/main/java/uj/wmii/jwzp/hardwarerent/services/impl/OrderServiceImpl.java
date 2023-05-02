package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Orders;
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
    public Orders addNewOrder(Orders order) {
        return ordersRepository.save(order);
    }

    @Override
    public Orders changeOrderInfo(Orders orders) {
        return null;
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }
    @Override
    public Optional<Orders> getOrderById(Long id) {
        return ordersRepository.findById(id);
    }
}

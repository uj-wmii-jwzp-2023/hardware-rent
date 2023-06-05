package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.OrderStatus;
import uj.wmii.jwzp.hardwarerent.repositories.OrdersRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Order createNewOrder(MyUser user, LocalDate orderDate, LocalDate dueDate, Set<OrderDetails> orderDetails) {
        if (orderDate.compareTo(dueDate) > 0)
            throw new InvalidDatesException("orderDate should be smaller than dueDate");
        Order order = new Order(user,orderDate,dueDate, orderDetails);
        return ordersRepository.save(order);
    }
    @Override
    public Order createNewOrder(Order order){
        return ordersRepository.save(order);
    }

    @Override
    public Optional<Order> changeOrderStatus(Long id, OrderStatus orderStatus) {
        Optional<Order> savedOrder = ordersRepository.findById(id);
        if (savedOrder.isEmpty())
            return Optional.empty();
        Order existing = savedOrder.get();
        existing.setOrderStatus(orderStatus);
        return Optional.of(ordersRepository.save(existing));

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

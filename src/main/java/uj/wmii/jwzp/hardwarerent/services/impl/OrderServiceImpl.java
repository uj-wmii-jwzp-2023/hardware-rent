package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Category;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.dto.CategoryDto;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDto;
import uj.wmii.jwzp.hardwarerent.repositories.OrdersRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderService;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Order createNewOrder(MyUser user, Date orderDate, Date dueDate, Set<OrderDetails> orderDetails) {
        if (orderDate.compareTo(dueDate) > 0)
            throw new InvalidDatesException("orderDate should be smaller than dueDate");
        Order order = new Order(user,orderDate,dueDate,null, orderDetails);
        return ordersRepository.save(order);
    }
    @Override
    public Order createNewOrder(Order order){
        return ordersRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(MyUser user) {
        return ordersRepository.findOrdersByUserEquals(user);
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
    public Set<OrderDetailsDto> getOrderDetailsDtoList(Order order){
        Set<OrderDetailsDto> orderDetailsDtoList = new HashSet<>();
        for (var orderDetails: order.getOrderDetails()) {
            orderDetailsDtoList.add(new OrderDetailsDto(orderDetails));
        }
        return orderDetailsDtoList;
    }
    @Override
    public List<OrderDto> getOrdersDtoList(List<Order> orders){
        List<OrderDto> ordersDtoList = new ArrayList<>();
        for (var order: orders) {
            //var orderDetailsDto = getOrderDetailsDtoList(order);
            var orderDto = new OrderDto(order);
            //orderDto.setOrderDetails(orderDetailsDto);
            ordersDtoList.add(orderDto);
        }
        return ordersDtoList;
    }
}

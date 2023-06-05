package uj.wmii.jwzp.hardwarerent.services.interfaces;
import uj.wmii.jwzp.hardwarerent.data.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {
    Order createNewOrder(MyUser user, LocalDate orderDate, LocalDate dueDate, Set<OrderDetails> orderDetails);
    Optional<Order> changeOrderStatus(Long id, OrderStatus orderStatus);
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createNewOrder(Order order);
}

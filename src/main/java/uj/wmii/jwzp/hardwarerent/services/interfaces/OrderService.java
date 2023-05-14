package uj.wmii.jwzp.hardwarerent.services.interfaces;
import uj.wmii.jwzp.hardwarerent.data.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {
    Order createNewOrder(MyUser user, Date orderDate, Date dueDate, Set<OrderDetails> orderDetails);
    Order changeOrderInfo(Order order);
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Order createNewOrder(Order order);
}

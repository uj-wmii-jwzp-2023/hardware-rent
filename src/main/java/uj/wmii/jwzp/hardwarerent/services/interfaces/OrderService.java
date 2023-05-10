package uj.wmii.jwzp.hardwarerent.services.interfaces;
import uj.wmii.jwzp.hardwarerent.data.*;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createNewOrder(Order order);
    Order changeOrderInfo(Order order);
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
}

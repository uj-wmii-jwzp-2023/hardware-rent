package uj.wmii.jwzp.hardwarerent.services;
import uj.wmii.jwzp.hardwarerent.data.*;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Orders addNewOrder(Orders orders);
    Orders changeOrderInfo(Orders orders);
    List<Orders> getAllOrders();
    Optional<Orders> getOrderById(Long id);
}

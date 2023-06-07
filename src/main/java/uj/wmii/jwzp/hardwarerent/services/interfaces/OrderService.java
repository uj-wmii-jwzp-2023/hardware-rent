package uj.wmii.jwzp.hardwarerent.services.interfaces;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> changeOrderStatus(Long id, String orderStatus);
    List<Order> getAllOrders(String orderStatus);
    Optional<Order> getOrderById(MyUser user, Long id);
    Optional<Order> createNewOrder(OrderDto orderDto, MyUser user, Clock clock);

    List<Order> getAllOrdersForUser(MyUser user, String orderStatus);

    void deleteOrderDetailFromOrder(MyUser user, Long orderId, Long orderDetailId);
    void addOrderDetailsToOrder(MyUser user, OrderDetailsDto orderDetailsDto);

    void payForProduct(MyUser user, Long id, String orderStatus);

    Optional<Order> getOrderFromAllById(Long id);
}

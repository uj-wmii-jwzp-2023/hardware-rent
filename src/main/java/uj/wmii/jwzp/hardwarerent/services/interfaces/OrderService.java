package uj.wmii.jwzp.hardwarerent.services.interfaces;
import org.springframework.security.core.Authentication;
import uj.wmii.jwzp.hardwarerent.data.*;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;

import javax.swing.text.html.Option;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderService {
    Optional<Order> changeOrderStatus(Long id, OrderStatus orderStatus);
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    Optional<Order> createNewOrder(OrderDto orderDto, MyUser user, Clock clock);

    List<Order> getAllOrdersForUser(MyUser user);

    boolean deleteOrderDetailFromOrder(MyUser user, Long orderId, Long orderDetailId);
    void addOrderDetailsToOrder(MyUser user, OrderDetailsDto orderDetailsDto);

    void payForProduct(MyUser user, Long id, String orderStatus);
}

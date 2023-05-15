package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;

import java.util.List;

public interface OrdersRepository  extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserEquals(MyUser user);
}

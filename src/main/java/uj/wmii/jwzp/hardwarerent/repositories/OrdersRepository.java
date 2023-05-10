package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.Order;

public interface OrdersRepository  extends JpaRepository<Order, Long> {
}

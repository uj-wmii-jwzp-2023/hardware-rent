package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;

import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findByOrder(Order order);
}

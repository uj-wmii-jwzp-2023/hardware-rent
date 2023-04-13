package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.Orders;

public interface OrdersRepository  extends JpaRepository<Orders, Long> {
}

package uj.wmii.jwzp.hardwarerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerental.data.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

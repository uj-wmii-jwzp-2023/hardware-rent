package uj.wmii.jwzp.hardwarerent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;

public interface OrderDetailsRepository  extends JpaRepository<OrderDetails, Long> {
}
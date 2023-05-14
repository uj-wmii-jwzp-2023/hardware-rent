package uj.wmii.jwzp.hardwarerent.services.interfaces;

import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDetailsDto;

import java.util.Set;

public interface OrderDetailsService {
    Set<OrderDetails> createOrderDetailsListFromOrderDetailsDtoList(Set<OrderDetailsDto> orderDetailsDtoSet, Order order);
}

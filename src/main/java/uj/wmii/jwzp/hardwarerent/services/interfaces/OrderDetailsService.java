package uj.wmii.jwzp.hardwarerent.services.interfaces;

import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDetailsDto;

import java.util.Set;

public interface OrderDetailsService {
    Set<OrderDetails> createOrderDetailsListFromOrderDetailsDtoListWithoutOrder(Set<OrderDetailsDto> orderDetailsDtoSet);
    Set<OrderDetails> createNewOrderDetails(Set<OrderDetails> orderDetails);

}

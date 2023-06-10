package uj.wmii.jwzp.hardwarerent.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class OrderDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String user;
    @Getter @Setter
    private String orderDate;
    @Getter @Setter
    private String dueDate;
    @Getter @Setter
    private Set<OrderDetailsDto> orderDetails;

    public OrderDto(Order order) {
        this.orderDetails = new HashSet<>();
        this.user = order.getUser().getUsername();
        this.orderDate = order.getOrderDate().toString();
        this.dueDate = order.getDueDate().toString();

        for (var orderDetail: order.getOrderDetails()) {
            this.orderDetails.add(new OrderDetailsDto(orderDetail));}
    }
}

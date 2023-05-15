package uj.wmii.jwzp.hardwarerent.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.MyUser;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;

@NoArgsConstructor
public class OrderDto {
    @JsonInclude(value= JsonInclude.Include.NON_EMPTY, content= JsonInclude.Include.NON_NULL)
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String user;
    @Getter @Setter
    private String orderDate;
    @Getter @Setter
    private String dueDate;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Set<OrderDetailsDto> orderDetails;

    public OrderDto(Order order) {
        this.user = order.getUser().getUsername();
        this.orderDate = order.getOrderDate().toString();
        this.orderDate = this.orderDate.substring(0,this.orderDate.length()-4);
        this.dueDate = order.getDueDate().toString();
        this.dueDate = this.dueDate.substring(0,this.dueDate.length()-2);
        this.description = (order.getDescription()== null) ? "no description": order.getDescription();

        this.orderDetails = new HashSet<>();
        for (var orderDetail: order.getOrderDetails()) {
            this.orderDetails.add(new OrderDetailsDto(orderDetail));}
    }
}

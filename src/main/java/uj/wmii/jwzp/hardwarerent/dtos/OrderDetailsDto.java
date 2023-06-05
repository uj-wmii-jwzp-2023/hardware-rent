package uj.wmii.jwzp.hardwarerent.dtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.Product;

@NoArgsConstructor
public class OrderDetailsDto {

    @Getter @Setter
    private Long orderDetails_id;
    @Getter @Setter
    private Long productId;
    @Getter @Setter
    private int quantity;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Long orderId;

    public OrderDetailsDto(OrderDetails orderDetails) {
        this.productId = orderDetails.getArchivedProducts().getProductId();
        this.description = (description == null) ? "no description": description;
        this.orderId = orderDetails.getOrder().getId();
    }
}

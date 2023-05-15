package uj.wmii.jwzp.hardwarerent.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;

@NoArgsConstructor
public class OrderDetailsDto {

    @Getter @Setter
    private Long orderDetails_id;
    @Getter @Setter
    private Long productId;
    @Getter @Setter
    private String productModel;
    @Getter @Setter
    private String productCompany;
    @Getter @Setter
    private String productCategoryName;
    @Getter @Setter
    private int quantity;

    @JsonIgnore
    @Getter @Setter
    private Long orderId;
    @Getter @Setter
    private float price; //price of object in orderDetail

    public OrderDetailsDto(OrderDetails orderDetails) {
        this.productId = orderDetails.getProduct().getId();
        this.productModel = orderDetails.getProduct().getModel();
        this.productCategoryName = orderDetails.getProduct().getCategory().getCategoryName();
        this.productCompany = orderDetails.getProduct().getCompanyName();
        this.quantity = orderDetails.getQuantity();
        this.orderId = orderDetails.getOrder().getId();
        this.price = orderDetails.getQuantity() * orderDetails.getProduct().getPrice().floatValue();
        //this.orderId = orderDetails.getOrder().getId();
    }
}

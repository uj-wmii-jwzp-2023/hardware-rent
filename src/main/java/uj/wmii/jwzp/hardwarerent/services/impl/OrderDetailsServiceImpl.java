package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.ProductNotFoundException;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.util.HashSet;
import java.util.Set;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final ProductService productService;

    public OrderDetailsServiceImpl(ProductService productService) {
        this.productService = productService;
    }
    public Set<OrderDetails> createOrderDetailsListFromOrderDetailsDtoListWithoutOrder(Set<OrderDetailsDto> orderDetailsDtoSet){
        Set<OrderDetails> orderDetailsSet= new HashSet<>();
        for (var orderDetailDto: orderDetailsDtoSet) {
            var product = productService.getProductById(orderDetailDto.getProductId());
                if(product.isEmpty())
                    throw new ProductNotFoundException("no product with id"+orderDetailDto.getProductId()+" was found");
            orderDetailsSet.add(new OrderDetails(product.get(),orderDetailDto.getQuantity(), orderDetailDto.getDescription(),null));
        }
        return orderDetailsSet;
    }

}

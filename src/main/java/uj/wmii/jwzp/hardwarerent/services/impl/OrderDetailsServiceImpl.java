package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.ProductNotFoundException;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.UnavailableProductQuantityException;
import uj.wmii.jwzp.hardwarerent.data.Order;
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
    public Set<OrderDetails> createOrderDetailsListFromOrderDetailsDtoList(Set<OrderDetailsDto> orderDetailsDtoSet, Order order){
        Set<OrderDetails> orderDetailsSet= new HashSet<>();
        for (var orderDetailDto: orderDetailsDtoSet) {
            var product = productService.getProductById(orderDetailDto.getProductId());
                if(product.isEmpty())
                    throw new ProductNotFoundException("no product with id"+orderDetailDto.getProductId()+" was found");
                else {
                    if(product.get().getAvailableQuantity() - orderDetailDto.getQuantity() < 0){
                        throw new UnavailableProductQuantityException("too many products:"+product.get().getModel()+"("+orderDetailDto.getQuantity()+")"+"."+" Available quantity:"+product.get().getAvailableQuantity());
                    }else {
                        product.get().setAvailableQuantity(product.get().getAvailableQuantity() - orderDetailDto.getQuantity());
                    }
                }
            orderDetailsSet.add(new OrderDetails(product.get(),orderDetailDto.getQuantity(), orderDetailDto.getDescription(),order));
        }
        return orderDetailsSet;
    }

}

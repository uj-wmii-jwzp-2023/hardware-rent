package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.ProductNotFoundException;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.data.dto.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.repositories.OrderDetailsRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.util.HashSet;
import java.util.Set;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductService productService;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository, ProductService productService) {
        this.orderDetailsRepository = orderDetailsRepository;
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

    @Override
    public Set<OrderDetails> createNewOrderDetails(Set<OrderDetails> orderDetails) {
        Set<OrderDetails> orderDetailsAdded = new HashSet<>();
        for (var orderDetail:orderDetails) {
            orderDetailsAdded.add(orderDetailsRepository.save(orderDetail));
        }
        return orderDetailsAdded;
    }

}

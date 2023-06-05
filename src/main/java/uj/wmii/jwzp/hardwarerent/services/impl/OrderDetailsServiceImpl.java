package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.data.ArchivedProducts;
import uj.wmii.jwzp.hardwarerent.data.Exceptions.ProductNotFoundException;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderDetails;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.mappers.ProductMapper;
import uj.wmii.jwzp.hardwarerent.repositories.ArchivedProductsRepository;
import uj.wmii.jwzp.hardwarerent.services.interfaces.OrderDetailsService;
import uj.wmii.jwzp.hardwarerent.services.interfaces.ProductService;

import java.util.HashSet;
import java.util.Set;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final ProductService productService;
    private final ProductMapper productMapper;

    private final ArchivedProductsRepository archivedProductsRepository;

    public OrderDetailsServiceImpl(ProductService productService, ProductMapper productMapper,
                                   ArchivedProductsRepository archivedProductsRepository) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.archivedProductsRepository = archivedProductsRepository;
    }
    public Set<OrderDetails> createOrderDetailsListFromOrderDetailsDtoList(Set<OrderDetailsDto> orderDetailsDtoSet, Order order){
        Set<OrderDetails> orderDetailsSet= new HashSet<>();
        for (var orderDetailDto: orderDetailsDtoSet) {
            var product = productService.getProductById(orderDetailDto.getProductId());
                if(product.isEmpty())
                    throw new ProductNotFoundException("no product with id"+orderDetailDto.getProductId()+" was found");
            ArchivedProducts archivedProducts = archivedProductsRepository.findArchivedProductsByProductId(
                    product.get().getId()).get();
            orderDetailsSet.add(new OrderDetails(archivedProducts, orderDetailDto.getDescription(),order));
        }
        return orderDetailsSet;
    }

}

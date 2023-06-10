package uj.wmii.jwzp.hardwarerent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderStatus;

import uj.wmii.jwzp.hardwarerent.dtos.OrderDetailsDto;
import uj.wmii.jwzp.hardwarerent.dtos.OrderDto;
import uj.wmii.jwzp.hardwarerent.exceptions.InvalidDatesException;
import uj.wmii.jwzp.hardwarerent.exceptions.NoEnoughMoneyException;
import uj.wmii.jwzp.hardwarerent.exceptions.NotFoundException;
import uj.wmii.jwzp.hardwarerent.exceptions.ProductAlreadyInOrderException;
import uj.wmii.jwzp.hardwarerent.repositories.ArchivedProductsRepository;
import uj.wmii.jwzp.hardwarerent.repositories.OrderDetailsRepository;
import uj.wmii.jwzp.hardwarerent.repositories.OrdersRepository;
import uj.wmii.jwzp.hardwarerent.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class OrdersControllerIntegrationTest {
    @Autowired
    OrdersController ordersController;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ArchivedProductsRepository archivedProductsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void testGetAllOrdersAdmin() {
        List<Order> orders = ordersController.getAllOrders(null);

        assertEquals(9, orders.size());
    }

    @Test
    void testGetByIdForAll() {
        Order test = ordersRepository.findAll().get(0);
        Order orderReturned = ordersController.getOrderByIdForAll(test.getId());

        assertNotNull(orderReturned);
        assertEquals(orderReturned.getOrderStatus(), OrderStatus.CREATED);

    }

    @WithMockUser("mladbago")
    @Test
    void testGetAllOrdersForUser() {
        List<Order> orders = ordersController.getAllOrdersForUser(null);

        assertEquals(3, orders.size());
    }

    @WithMockUser("mladbago")
    @Test
    void testGetByIdForUser() {
        Order orderReturned = ordersController.getOrderById(7L);

        assertNotNull(orderReturned);
    }

    @WithMockUser("mladbago")
    @Test
    void testGetAllOrdersForUserByOrderStatus() {
        List<Order> orderReturned = ordersController.getAllOrdersForUser("INITIALIZED");

        assertEquals(3, orderReturned.size());
    }

    @Transactional
    @Rollback
    @WithMockUser("mladbago")
    @Test
    void testPostOrderWithValidPropertiesSavesOrder() throws Exception {
        OrderDetailsDto first = new OrderDetailsDto(12L, null);
        OrderDetailsDto second = new OrderDetailsDto(13L, null);
        OrderDetailsDto third = new OrderDetailsDto(14L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);
        orderDetailsDtoSet.add(second);
        orderDetailsDtoSet.add(third);

        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateForPostValid(dto);

        ordersController.createNewOrder(dto);

        System.out.println(ordersRepository.findAll().get(9).getOrderDetails().stream()
                .map(x -> x.getArchivedProducts().getProductId()).toList().toString());
        assertNotNull(ordersRepository.findAll().get(9));
        assertEquals(6, orderDetailsRepository.findAll().size());

    }

    @WithMockUser
    @Test
    void testPostOrderWithOverlappingDatesThrowsException() {
        OrderDetailsDto first = new OrderDetailsDto(12L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);
        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateForPostValid(dto);

    }

    @Test
    @WithMockUser("admin")
    void testPostOrderWithOrderDateBiggerThanDueDateThrowsException() {
        OrderDetailsDto first = new OrderDetailsDto(12L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);
        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateOrderDateBiggerThanDueDate(dto);

        assertThrows(InvalidDatesException.class,
                () -> ordersController.createNewOrder(dto));
    }

    @Test
    @WithMockUser("admin")
    void testPostOrderWithOrderDateLessThanTodaysDateThrowsException() {
        OrderDetailsDto first = new OrderDetailsDto(12L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);
        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateOrderDateSmallerThanTodaysDate(dto);

        assertThrows(InvalidDatesException.class,
                () -> ordersController.createNewOrder(dto));
    }

    @WithMockUser("admin")
    @Test
    void testPostOrderWithProductDuplicatesThrowsException() {
        OrderDetailsDto first = new OrderDetailsDto(12L, null);
        OrderDetailsDto second = new OrderDetailsDto(13L, null);
        OrderDetailsDto third = new OrderDetailsDto(12L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);
        orderDetailsDtoSet.add(second);
        orderDetailsDtoSet.add(third);

        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateForPostValid(dto);

        assertThrows(ProductAlreadyInOrderException.class,
                () -> ordersController.createNewOrder(dto));
    }

    @Test
    @WithMockUser("admin")
    void testPostProductWithProductNotInProductsTableThrowsException() {
        OrderDetailsDto first = new OrderDetailsDto(18L, null);
        OrderDetailsDto second = new OrderDetailsDto(13L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);
        orderDetailsDtoSet.add(second);

        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateForPostValid(dto);

        assertThrows(NotFoundException.class,
                () -> ordersController.createNewOrder(dto));

    }

    @Test
    @WithMockUser("mladbago")
    void testPostProductWithOverlappingDates() {
        OrderDetailsDto first = new OrderDetailsDto(1L, null);
        Set<OrderDetailsDto> orderDetailsDtoSet = new HashSet<>();
        orderDetailsDtoSet.add(first);

        OrderDto dto = new OrderDto();
        dto.setOrderDetails(orderDetailsDtoSet);
        createDateForPostNotValid(dto);

        assertThrows(InvalidDatesException.class,
                () -> ordersController.createNewOrder(dto));
    }
    @Test
    @Transactional
    @Rollback
    @WithMockUser("admin")
    void testDeleteOrderDetailFromOrderWithValidProperties() throws Exception {
        mockMvc.perform(delete("/orders/1/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(0, ordersRepository.findById(1L).get().getOrderDetails().size());
        assertNull(orderDetailsRepository.findById(1L).orElse(null));
    }

    @Test
    @WithMockUser("admin")
    void testDeleteOrderDetailFromOrderWithNotExistingOrderThrowsException() {
        assertThrows(NotFoundException.class,
                () -> ordersController.deleteOrderDetailFromOrder(15L, 1L));
    }

    @Test
    @WithMockUser("admin")
    void testDeleteOrderDetailFromOrderWithNotExistingOrderDetailThrowsException() {
        assertThrows(NotFoundException.class,
                () -> ordersController.deleteOrderDetailFromOrder(1L, 4L));
    }
    @Test
    @WithMockUser("admin")
    void testAddOrderDetailToOrder() throws Exception {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(2L, null);
        orderDetailsDto.setOrderId(2L);
        ordersController.addOrderDetailToOrder(orderDetailsDto);

        assertEquals(1, ordersRepository.findById(2L).get().getOrderDetails().size());
        assertNotEquals(new BigDecimal(0), ordersRepository.findById(2L).get().getOverallCashSum());
    }

    @Test
    @Transactional
    @Rollback
    @WithMockUser("mladbago")
    void testPayProductWithNoMoneyThrowsNoMoneyException() {
        ordersRepository.findById(1L).get().setOrderStatus(OrderStatus.INITIALIZED);
        assertThrows(NoEnoughMoneyException.class,
                () -> ordersController.payForOrder(7L, "CREATED"));
    }

    @Test
    @Transactional
    @Rollback
    @WithMockUser("admin")
    void testPayProductSuccessful() {
        ordersRepository.findById(1L).get().setOrderStatus(OrderStatus.INITIALIZED);
        ordersController.payForOrder(1L, "CREATED");
        assertEquals(new BigDecimal("99999999942.80"), userRepository.findByUsername("admin").getCash());
    }
    public void createDateForPostValid(OrderDto orderDto) {
        Clock clock = Clock.systemUTC();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentDate = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Europe/Warsaw"));
        LocalDateTime futureDateTimeOrder;
        LocalDateTime futureDateTimeDue;
        LocalDate futureDate = currentDate.plusDays(14).toLocalDate();
        LocalTime futureTime = LocalTime.of(13, 0);
        futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
        futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(10), futureTime);
        orderDto.setOrderDate(futureDateTimeOrder.format(formatter));
        orderDto.setDueDate(futureDateTimeDue.format(formatter));
    }

    public void createDateForPostNotValid(OrderDto orderDto) {
        Clock clock = Clock.systemUTC();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentDate = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Europe/Warsaw"));
        LocalDate futureDate = currentDate.plusDays(12).toLocalDate();
        LocalTime futureTime = LocalTime.of(12, 59);
        LocalDateTime futureDateTimeOrder = LocalDateTime.of(futureDate, futureTime);
        LocalDateTime futureDateTimeDue = LocalDateTime.of(futureDate.plusDays(10), futureTime);
        orderDto.setOrderDate(futureDateTimeOrder.format(formatter));
        orderDto.setDueDate(futureDateTimeDue.format(formatter));
    }

    public void createDateOrderDateBiggerThanDueDate(OrderDto orderDto) {
        Clock clock = Clock.systemUTC();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentDate = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Europe/Warsaw"));
        LocalDateTime futureDateTimeOrder;
        LocalDateTime futureDateTimeDue;
        LocalDateTime futureDate = currentDate.plusDays(13);
        LocalTime futureTime = LocalTime.of(13, 0);
        futureDateTimeOrder = LocalDateTime.of(futureDate.toLocalDate(), futureTime);
        futureDateTimeDue = LocalDateTime.of(futureDate.toLocalDate(), futureTime.minusMinutes(1));
        orderDto.setOrderDate(futureDateTimeOrder.format(formatter));
        orderDto.setDueDate(futureDateTimeDue.format(formatter));
    }

    public void createDateOrderDateSmallerThanTodaysDate(OrderDto orderDto) {
        Clock clock = Clock.systemUTC();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentDate = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Europe/Warsaw"));
        LocalDateTime futureDateTimeOrder = currentDate.minusMinutes(1);
        LocalDateTime futureTimeOrder= currentDate.minusMinutes(1);
        futureDateTimeOrder = LocalDateTime.of(futureDateTimeOrder.toLocalDate(), futureTimeOrder.toLocalTime());
        LocalDateTime futureDateTimeDue = LocalDateTime.of(futureDateTimeOrder.toLocalDate().plusDays(14),
                futureTimeOrder.toLocalTime());
        orderDto.setOrderDate(futureDateTimeOrder.format(formatter));
        orderDto.setDueDate(futureDateTimeDue.format(formatter));
    }
}
package uj.wmii.jwzp.hardwarerent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import uj.wmii.jwzp.hardwarerent.data.Order;
import uj.wmii.jwzp.hardwarerent.data.OrderStatus;
import uj.wmii.jwzp.hardwarerent.repositories.OrdersRepository;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduledConfig {


    private final OrdersRepository ordersRepository;
    private final Clock clock;
    private final Logger logger = LoggerFactory.getLogger(ScheduledConfig.class);
    public ScheduledConfig(OrdersRepository ordersRepository, Clock clock) {
        this.ordersRepository = ordersRepository;
        this.clock = clock;
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateTask() {
        List<Order> orders = ordersRepository.findAll();
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(clock.instant(), ZoneId.of("Europe/Warsaw"));

        for (Order order: orders) {
            if (Duration.between(order.getCreatedOn(), currentDateTime).toMinutes() > 10
            && order.getOrderStatus() == OrderStatus.INITIALIZED) {
                order.setOrderStatus(OrderStatus.CANCELED);
                logger.info("Entered to change order status!");
                ordersRepository.save(order);
            }
        }
        logger.info("Entered");
    }
}

package org.orderhub.sc.ScheduledOrder.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrderItem;
import org.orderhub.sc.ScheduledOrder.repository.ScheduledOrderRepository;
import org.orderhub.sc.ScheduledOrder.service.ScheduledOrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final ScheduledOrderService scheduledOrderService;

    @KafkaListener(topics = "order-created", groupId = "scheduled-order-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEventRequest request) {
        log.info("Received order event: {}", request);
        scheduledOrderService.save(request);
    }

    @KafkaListener(topics = "order-updated", groupId = "scheduled-order-group", containerFactory = "kafkaListenerContainerFactory")
    public void updateListen(OrderEventRequest request) {
        log.info("Received update order event: {}", request);
        scheduledOrderService.update(request);
    }

}

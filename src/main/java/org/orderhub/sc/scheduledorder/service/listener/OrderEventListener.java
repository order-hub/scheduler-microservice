package org.orderhub.sc.scheduledorder.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orderhub.sc.scheduledorder.service.ScheduledOrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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

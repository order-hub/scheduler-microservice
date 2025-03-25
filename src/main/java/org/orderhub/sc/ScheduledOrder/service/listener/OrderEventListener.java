package org.orderhub.sc.ScheduledOrder.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrderItem;
import org.orderhub.sc.ScheduledOrder.repository.ScheduledOrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final ScheduledOrderRepository scheduledOrderRepository;

    @Transactional
    @KafkaListener(topics = "order-created", groupId = "scheduled-order-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderEventRequest request) {
        log.info("Received order event: {}", request);

        ScheduledOrder order = ScheduledOrder.builder()
                .originalOrderId(request.getOrderId())
                .storeId(request.getStoreId())
                .status(request.getStatus())
                .orderCreatedAt(request.getCreatedAt())
                .items(request.getItems().stream()
                        .map(item -> ScheduledOrderItem.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .quantity(item.getQuantity())
                                .pricePerUnit(String.valueOf(item.getPrice()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
        scheduledOrderRepository.save(order);
    }
}

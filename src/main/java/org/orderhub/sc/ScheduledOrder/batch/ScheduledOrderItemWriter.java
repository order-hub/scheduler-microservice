package org.orderhub.sc.ScheduledOrder.batch;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.OutboxEvent.domain.AggregateType;
import org.orderhub.sc.OutboxEvent.domain.EventType;
import org.orderhub.sc.OutboxEvent.dto.request.OutBoxEventCreateRequest;
import org.orderhub.sc.OutboxEvent.service.OutboxEventService;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.orderhub.sc.ScheduledOrder.dto.request.InventoryDeductRequest;
import org.orderhub.sc.ScheduledOrder.dto.request.InventoryItemRequest;
import org.orderhub.sc.ScheduledOrder.repository.ScheduledOrderRepository;
import org.orderhub.sc.ScheduledOrder.util.SerialNumberGenerator;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduledOrderItemWriter implements ItemWriter<ScheduledOrder> {

    private final ScheduledOrderRepository scheduledOrderRepository;
    private final OutboxEventService outboxEventService;

    @Override
    @Transactional
    public void write(Chunk<? extends ScheduledOrder> orders) throws Exception {
        scheduledOrderRepository.saveAll(orders);

        for (ScheduledOrder order : orders) {
            InventoryDeductRequest event = InventoryDeductRequest.builder()
                    .storeId(order.getStoreId())
                    .items(order.getItems().stream()
                            .map(item -> new InventoryItemRequest(item.getProductId(), item.getQuantity()))
                            .collect(Collectors.toList()))
                    .build();

            outboxEventService.createOutboxEvent(OutBoxEventCreateRequest.builder()
                            .aggregateId(SerialNumberGenerator.generateOutBoxEventNumber(EventType.ORDER_SCHEDULED, order.getId()))
                            .aggregateType(AggregateType.SCHEDULED_ORDER)
                            .eventType(EventType.ORDER_SCHEDULED)
                            .payload(event.toString())
                            .publishedAt(Instant.now())
                    .build());
        }
    }
}

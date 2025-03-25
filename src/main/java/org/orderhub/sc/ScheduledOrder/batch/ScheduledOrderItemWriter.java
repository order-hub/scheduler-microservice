package org.orderhub.sc.ScheduledOrder.batch;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.orderhub.sc.ScheduledOrder.dto.request.InventoryDeductRequest;
import org.orderhub.sc.ScheduledOrder.dto.request.InventoryItemRequest;
import org.orderhub.sc.ScheduledOrder.repository.ScheduledOrderRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduledOrderItemWriter implements ItemWriter<ScheduledOrder> {

    private final ScheduledOrderRepository scheduledOrderRepository;
    private final KafkaTemplate<String, InventoryDeductRequest> kafkaTemplate;

    @Override
    public void write(Chunk<? extends ScheduledOrder> orders) throws Exception {
        scheduledOrderRepository.saveAll(orders);

        for (ScheduledOrder order : orders) {
            InventoryDeductRequest event = InventoryDeductRequest.builder()
                    .storeId(order.getStoreId())
                    .items(order.getItems().stream()
                            .map(item -> new InventoryItemRequest(item.getProductId(), item.getQuantity()))
                            .collect(Collectors.toList()))
                    .build();

            kafkaTemplate.send("inventory-deduct", event);
        }
    }
}

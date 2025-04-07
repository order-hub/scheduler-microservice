package org.orderhub.sc.scheduledorder.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.orderhub.sc.outboxevent.domain.AggregateType;
import org.orderhub.sc.outboxevent.domain.EventType;
import org.orderhub.sc.outboxevent.dto.request.OutBoxEventCreateRequest;
import org.orderhub.sc.outboxevent.service.OutboxEventService;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrder;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrderItem;
import org.orderhub.sc.scheduledorder.repository.ScheduledOrderRepository;
import org.springframework.batch.item.Chunk;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ScheduledOrderItemWriterTest {

    private ScheduledOrderRepository scheduledOrderRepository;
    private OutboxEventService outboxEventService;
    private ScheduledOrderItemWriter itemWriter;

    @BeforeEach
    void setUp() {
        scheduledOrderRepository = mock(ScheduledOrderRepository.class);
        outboxEventService = mock(OutboxEventService.class);
        itemWriter = new ScheduledOrderItemWriter(scheduledOrderRepository, outboxEventService);
    }

    @Test
    void shouldSaveAllOrdersAndCreateOutboxEvents() throws Exception {
        // Given
        ScheduledOrderItem item1 = ScheduledOrderItem.builder()
                .productId(1L)
                .quantity(2)
                .build();

        ScheduledOrder order1 = ScheduledOrder.builder()
                .storeId(10L)
                .items(List.of(item1))
                .build();

        ScheduledOrder order2 = ScheduledOrder.builder()
                .storeId(20L)
                .items(List.of(
                        ScheduledOrderItem.builder()
                                .productId(2L)
                                .quantity(1)
                                .build()
                ))
                .build();

        // When
        itemWriter.write(Chunk.of(order1, order2));

        // Then
        ArgumentCaptor<Chunk<? extends ScheduledOrder>> chunkCaptor =
                ArgumentCaptor.forClass(Chunk.class);
        verify(scheduledOrderRepository, times(1)).saveAll(chunkCaptor.capture());

        List<? extends ScheduledOrder> savedOrders = chunkCaptor.getValue().getItems();

        verify(outboxEventService, times(2)).createOutboxEvent(any());

        ArgumentCaptor<OutBoxEventCreateRequest> eventCaptor = ArgumentCaptor.forClass(OutBoxEventCreateRequest.class);
        verify(outboxEventService, times(2)).createOutboxEvent(eventCaptor.capture());

        List<OutBoxEventCreateRequest> calledEvents = eventCaptor.getAllValues();
        for (OutBoxEventCreateRequest request : calledEvents) {
            assertThat(request.getAggregateType()).isEqualTo(AggregateType.SCHEDULED_ORDER);
            assertThat(request.getEventType()).isEqualTo(EventType.ORDER_SCHEDULED);
            assertThat(request.getAggregateId()).startsWith("ORDER_SCHEDULED_");
            assertThat(request.getPayload()).contains("storeId=");
        }
    }

}

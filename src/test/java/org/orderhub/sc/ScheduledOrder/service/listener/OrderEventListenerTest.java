package org.orderhub.sc.ScheduledOrder.service.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orderhub.sc.ScheduledOrder.domain.OrderStatus;
import org.orderhub.sc.ScheduledOrder.domain.ProcessStatus;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrderItem;
import org.orderhub.sc.ScheduledOrder.repository.ScheduledOrderRepository;
import org.orderhub.sc.ScheduledOrder.service.ScheduledOrderService;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private ScheduledOrderService scheduledOrderService;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Test
    void shouldMapAllFieldsCorrectly() {
        // Given
        OrderEventRequest request = OrderEventRequest.builder()
                .orderId(12345L)
                .storeId(67890L)
                .memberId(9999L)
                .status(OrderStatus.PENDING)
                .createdAt(Instant.parse("2024-01-01T00:00:00Z"))
                .items(List.of(
                        OrderItemResponse.builder()
                                .productId(1L)
                                .productName("맥북 프로 16인치")
                                .price(3_500_000)
                                .quantity(1)
                                .categoryId(100L)
                                .categoryName("노트북")
                                .imageUrl("image-url.jpg")
                                .build(),
                        OrderItemResponse.builder()
                                .productId(2L)
                                .productName("무선 마우스")
                                .price(25_000)
                                .quantity(2)
                                .build()
                ))
                .build();

        // When
        orderEventListener.listen(request);

        // Then
        verify(scheduledOrderService, times(1)).save(request);
    }

}
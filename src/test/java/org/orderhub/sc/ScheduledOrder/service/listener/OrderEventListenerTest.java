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

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private ScheduledOrderRepository scheduledOrderRepository;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Captor
    private ArgumentCaptor<ScheduledOrder> orderCaptor;

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
        verify(scheduledOrderRepository).save(orderCaptor.capture());
        ScheduledOrder savedOrder = orderCaptor.getValue();

        // 기본 필드 검증
        assertThat(savedOrder.getOriginalOrderId()).isEqualTo(request.getOrderId());
        assertThat(savedOrder.getStoreId()).isEqualTo(request.getStoreId());
        assertThat(savedOrder.getStatus()).isEqualTo(request.getStatus());
        assertThat(savedOrder.getOrderCreatedAt()).isEqualTo(request.getCreatedAt());

        // 자동 생성 필드 검증
        assertThat(savedOrder.getScheduledAt()).isNotNull();
        assertThat(savedOrder.getProcessStatus()).isEqualTo(ProcessStatus.PENDING);

        // 항목 검증
        List<ScheduledOrderItem> items = savedOrder.getItems();
        assertThat(items).hasSize(2);

        ScheduledOrderItem firstItem = items.get(0);
        assertThat(firstItem.getProductId()).isEqualTo(1L);
        assertThat(firstItem.getProductName()).isEqualTo("맥북 프로 16인치");
        assertThat(firstItem.getQuantity()).isEqualTo(1);
        assertThat(firstItem.getPricePerUnit()).isEqualTo("3500000");
    }

}
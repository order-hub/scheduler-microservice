package org.orderhub.sc.scheduledorder.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.orderhub.sc.scheduledorder.domain.OrderStatus;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrder;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrderItem;
import org.orderhub.sc.scheduledorder.repository.ScheduledOrderRepository;
import org.orderhub.sc.scheduledorder.service.listener.OrderEventRequest;
import org.orderhub.sc.scheduledorder.service.listener.OrderItemResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ScheduledOrderServiceImplTest {

    @Mock
    private ScheduledOrderRepository scheduledOrderRepository;

    @InjectMocks
    private ScheduledOrderServiceImpl scheduledOrderService;

    @Test
    void save_ShouldCallRepositorySaveWithMappedEntity() {
        // Given
        OrderEventRequest request = OrderEventRequest.builder()
                .orderId(1L)
                .storeId(10L)
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .items(List.of(
                        OrderItemResponse.builder()
                                .productId(101L)
                                .productName("상품1")
                                .price(10000)
                                .quantity(2)
                                .build()
                ))
                .build();

        // When
        scheduledOrderService.save(request);

        // Then
        ArgumentCaptor<ScheduledOrder> captor = ArgumentCaptor.forClass(ScheduledOrder.class);
        verify(scheduledOrderRepository, times(1)).save(captor.capture());

        ScheduledOrder saved = captor.getValue();
        assertThat(saved.getOriginalOrderId()).isEqualTo(1L);
        assertThat(saved.getItems()).hasSize(1);
        ScheduledOrderItem item = saved.getItems().get(0);
        assertThat(item.getProductId()).isEqualTo(101L);
        assertThat(item.getProductName()).isEqualTo("상품1");
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getPricePerUnit()).isEqualTo("10000");
    }

    @Test
    void update_ShouldCallUpdateOnExistingScheduledOrder() {
        // Given
        OrderEventRequest request = OrderEventRequest.builder()
                .orderId(1L)
                .status(OrderStatus.DELIVERED)
                .build();

        ScheduledOrder existingOrder = mock(ScheduledOrder.class);
        when(scheduledOrderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        // When
        scheduledOrderService.update(request);

        // Then
        verify(existingOrder, times(1)).update(request);
    }

    @Test
    void update_ShouldThrowException_WhenOrderNotFound() {
        // Given
        OrderEventRequest request = OrderEventRequest.builder()
                .orderId(999L)
                .build();

        when(scheduledOrderRepository.findById(999L)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> scheduledOrderService.update(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order not found");
    }
}

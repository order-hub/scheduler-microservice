package org.orderhub.sc.scheduledorder.domain;

import org.junit.jupiter.api.Test;
import org.orderhub.sc.scheduledorder.service.listener.OrderEventRequest;
import org.orderhub.sc.scheduledorder.service.listener.OrderItemResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduledOrderTest {

    @Test
    void shouldBuildScheduledOrderCorrectly() {
        ScheduledOrderItem item = ScheduledOrderItem.builder()
                .productId(1L)
                .productName("노트북")
                .quantity(2)
                .pricePerUnit("1500000")
                .build();

        ScheduledOrder order = ScheduledOrder.builder()
                .originalOrderId(123L)
                .storeId(456L)
                .status(OrderStatus.PENDING)
                .orderCreatedAt(Instant.parse("2024-01-01T00:00:00Z"))
                .items(List.of(item))
                .build();

        assertThat(order.getOriginalOrderId()).isEqualTo(123L);
        assertThat(order.getStoreId()).isEqualTo(456L);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getOrderCreatedAt()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getProcessStatus()).isEqualTo(ProcessStatus.PENDING);
    }

    @Test
    void shouldMarkAsProcessed() {
        ScheduledOrder order = ScheduledOrder.builder()
                .originalOrderId(1L)
                .storeId(1L)
                .status(OrderStatus.PENDING)
                .orderCreatedAt(Instant.now())
                .items(List.of())
                .build();

        order.markAsProcessed();

        assertThat(order.getProcessStatus()).isEqualTo(ProcessStatus.PROCESSING);
    }

    @Test
    void shouldUpdateFieldsAndItems() {
        ScheduledOrderItem oldItem = ScheduledOrderItem.builder()
                .productId(1L)
                .productName("기존 상품")
                .quantity(1)
                .pricePerUnit("1000")
                .build();

        ScheduledOrder order = ScheduledOrder.builder()
                .originalOrderId(1L)
                .storeId(1L)
                .status(OrderStatus.PENDING)
                .orderCreatedAt(Instant.now())
                .items(new ArrayList<>(List.of(oldItem)))
                .build();

        Instant newCreatedAt = Instant.parse("2025-01-01T00:00:00Z");
        OrderItemResponse orderItemResponse = OrderItemResponse.builder()
                .productId(99L)
                .productName("새 상품")
                .quantity(3)
                .price(10000)
                .build();
        OrderEventRequest updateRequest = OrderEventRequest.builder()
                .orderId(1L)
                .storeId(1L)
                .memberId(999L)
                .status(OrderStatus.DELIVERED)
                .createdAt(newCreatedAt)
                .items(new ArrayList<>(List.of(orderItemResponse)))
                .build();

        order.update(updateRequest);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(order.getOrderCreatedAt()).isEqualTo(newCreatedAt);
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems().get(0).getProductId()).isEqualTo(99L);
        assertThat(order.getItems().get(0).getProductName()).isEqualTo("새 상품");
        assertThat(order.getItems().get(0).getQuantity()).isEqualTo(3);
        assertThat(order.getItems().get(0).getPricePerUnit()).isEqualTo("10000");
    }
}

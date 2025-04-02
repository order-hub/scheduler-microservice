package org.orderhub.sc.scheduledorder.service.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orderhub.sc.scheduledorder.domain.OrderStatus;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventRequest {
    private Long orderId;
    private Long storeId;
    private Long memberId;
    private OrderStatus status;
    private Instant createdAt;
    private List<OrderItemResponse> items;
}
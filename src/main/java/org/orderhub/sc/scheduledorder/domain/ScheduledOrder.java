package org.orderhub.sc.scheduledorder.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.orderhub.sc.scheduledorder.service.listener.OrderEventRequest;
import org.orderhub.sc.scheduledorder.util.OrderStatusConverter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "scheduled_orders")
@NoArgsConstructor
public class ScheduledOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long originalOrderId;

    @Column(nullable = false)
    private Long storeId;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant orderCreatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "scheduled_order_id")
    private List<ScheduledOrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Instant scheduledAt = Instant.now();

    @Column
    private Instant processedAt;

    @Enumerated(EnumType.STRING)
    private ProcessStatus processStatus = ProcessStatus.PENDING;

    @Builder
    public ScheduledOrder(Long originalOrderId, Long storeId, OrderStatus status, Instant orderCreatedAt, List<ScheduledOrderItem> items) {
        this.originalOrderId = originalOrderId;
        this.storeId = storeId;
        this.status = status;
        this.orderCreatedAt = orderCreatedAt;
        this.items = new ArrayList<>(items);
    }

    public void markAsProcessed() {
        this.processStatus = ProcessStatus.PROCESSING;
    }

    public void update(OrderEventRequest request) {
        this.status = request.getStatus();
        this.orderCreatedAt = request.getCreatedAt();

        this.items.clear();
        List<ScheduledOrderItem> updatedItems = request.getItems().stream()
                .map(item -> ScheduledOrderItem.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .pricePerUnit(String.valueOf(item.getPrice()))
                        .build())
                .toList();
        this.items.addAll(updatedItems);
    }

}

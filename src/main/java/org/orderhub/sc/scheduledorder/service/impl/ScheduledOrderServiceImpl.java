package org.orderhub.sc.scheduledorder.service.impl;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrder;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrderItem;
import org.orderhub.sc.scheduledorder.repository.ScheduledOrderRepository;
import org.orderhub.sc.scheduledorder.service.ScheduledOrderService;
import org.orderhub.sc.scheduledorder.service.listener.OrderEventRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduledOrderServiceImpl implements ScheduledOrderService {
    private final ScheduledOrderRepository scheduledOrderRepository;

    @Transactional
    public void save(OrderEventRequest request) {
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

    @Transactional
    public void update(OrderEventRequest request) {
        ScheduledOrder scheduledOrder = findById(request.getOrderId());
        scheduledOrder.update(request);
    }

    private ScheduledOrder findById(long id) {
        return scheduledOrderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}

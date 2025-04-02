package org.orderhub.sc.ScheduledOrder.service.impl;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrderItem;
import org.orderhub.sc.ScheduledOrder.repository.ScheduledOrderRepository;
import org.orderhub.sc.ScheduledOrder.service.ScheduledOrderService;
import org.orderhub.sc.ScheduledOrder.service.listener.OrderEventRequest;
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
}

package org.orderhub.sc.ScheduledOrder.service;

import org.orderhub.sc.ScheduledOrder.service.listener.OrderEventRequest;

public interface ScheduledOrderService {
    void save(OrderEventRequest request);
}

package org.orderhub.sc.scheduledorder.service;

import org.orderhub.sc.scheduledorder.service.listener.OrderEventRequest;

public interface ScheduledOrderService {
    void save(OrderEventRequest request);
    void update(OrderEventRequest request);
}

package org.orderhub.sc.OutboxEvent.service;

import org.orderhub.sc.OutboxEvent.dto.request.OutBoxEventCreateRequest;

public interface OutboxEventService {
    void createOutboxEvent(OutBoxEventCreateRequest request);
}

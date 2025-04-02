package org.orderhub.sc.outboxevent.service;

import org.orderhub.sc.outboxevent.dto.request.OutBoxEventCreateRequest;

public interface OutboxEventService {
    void createOutboxEvent(OutBoxEventCreateRequest request);
}

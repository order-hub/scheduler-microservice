package org.orderhub.sc.outboxevent.service.impl;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.outboxevent.domain.OutboxEvent;
import org.orderhub.sc.outboxevent.dto.request.OutBoxEventCreateRequest;
import org.orderhub.sc.outboxevent.respository.OutBoxEventRepository;
import org.orderhub.sc.outboxevent.service.OutboxEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutBoxEventServiceImpl implements OutboxEventService {
    private final OutBoxEventRepository outBoxEventRepository;

    public void createOutboxEvent(OutBoxEventCreateRequest request) {
        outBoxEventRepository.save(OutboxEvent.from(request));
    }

}

package org.orderhub.sc.OutboxEvent.service.impl;

import lombok.RequiredArgsConstructor;
import org.orderhub.sc.OutboxEvent.domain.OutboxEvent;
import org.orderhub.sc.OutboxEvent.dto.request.OutBoxEventCreateRequest;
import org.orderhub.sc.OutboxEvent.respository.OutBoxEventRepository;
import org.orderhub.sc.OutboxEvent.service.OutboxEventService;
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

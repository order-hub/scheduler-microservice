package org.orderhub.sc.outboxevent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orderhub.sc.outboxevent.domain.AggregateType;
import org.orderhub.sc.outboxevent.domain.EventStatus;
import org.orderhub.sc.outboxevent.domain.EventType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutBoxEventCreateRequest {
    private AggregateType aggregateType;
    private String aggregateId;
    private EventType eventType;
    private String payload;
    private EventStatus status;
    private Instant publishedAt = Instant.now();
}

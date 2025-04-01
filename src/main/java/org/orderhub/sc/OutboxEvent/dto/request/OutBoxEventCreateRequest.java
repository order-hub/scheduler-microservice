package org.orderhub.sc.OutboxEvent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orderhub.sc.OutboxEvent.domain.AggregateType;
import org.orderhub.sc.OutboxEvent.domain.EventStatus;
import org.orderhub.sc.OutboxEvent.domain.EventType;

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

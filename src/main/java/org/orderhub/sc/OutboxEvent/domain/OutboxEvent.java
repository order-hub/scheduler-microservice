package org.orderhub.sc.OutboxEvent.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.orderhub.sc.OutboxEvent.dto.request.OutBoxEventCreateRequest;

import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;
    private String aggregateId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    private Instant publishedAt;

    @Builder
    public OutboxEvent(AggregateType aggregateType, String aggregateId, EventType eventType, String payload, EventStatus status, Instant publishedAt) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.publishedAt = publishedAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public static OutboxEvent from(OutBoxEventCreateRequest outboxEvent) {
        return OutboxEvent.builder()
                .aggregateType(outboxEvent.getAggregateType())
                .aggregateId(outboxEvent.getAggregateId())
                .eventType(outboxEvent.getEventType())
                .payload(outboxEvent.getPayload())
                .status(outboxEvent.getStatus())
                .publishedAt(outboxEvent.getPublishedAt())
                .build();
    }

}

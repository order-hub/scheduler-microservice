package org.orderhub.sc.ScheduledOrder.util;

import org.orderhub.sc.OutboxEvent.domain.EventType;

import java.util.UUID;

public class SerialNumberGenerator {
    public static String generateOutBoxEventNumber(EventType eventType, Long eventId) {
        return eventType.name() + "_" + eventId + "_" + UUID.randomUUID().toString();
    }
}

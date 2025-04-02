package org.orderhub.sc.scheduledorder.util;

import org.orderhub.sc.outboxevent.domain.EventType;

import java.util.UUID;

public class SerialNumberGenerator {
    public static String generateOutBoxEventNumber(EventType eventType, Long eventId) {
        return eventType.name() + "_" + eventId + "_" + UUID.randomUUID().toString();
    }
}

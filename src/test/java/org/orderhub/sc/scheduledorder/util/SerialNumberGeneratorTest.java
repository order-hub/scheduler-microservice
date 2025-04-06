package org.orderhub.sc.scheduledorder.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.orderhub.sc.outboxevent.domain.EventType;

import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class SerialNumberGeneratorTest {

    @Test
    void shouldGenerateOutBoxEventNumberWithCorrectFormat() {
        // Given
        EventType eventType = EventType.ORDER_SCHEDULED;
        Long eventId = 123L;

        // When
        String serial = SerialNumberGenerator.generateOutBoxEventNumber(eventType, eventId);

        // Then
        String[] parts = serial.split("_");
        assertThat(parts).hasSize(4);
        assertThat(parts[0]+"_"+parts[1]).isEqualTo(eventType.name());
        assertThat(parts[2]).isEqualTo(String.valueOf(eventId));

        // UUID 형식 확인 (8-4-4-4-12)
        assertThat(Pattern.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", parts[3]))
                .isTrue();
    }

    @Test
    void shouldGenerateDifferentValuesEachTime() {
        // Given
        EventType eventType = EventType.ORDER_SCHEDULED;
        Long eventId = 123L;

        // When
        String serial1 = SerialNumberGenerator.generateOutBoxEventNumber(eventType, eventId);
        String serial2 = SerialNumberGenerator.generateOutBoxEventNumber(eventType, eventId);

        // Then
        assertThat(serial1).isNotEqualTo(serial2);
    }

}
package org.orderhub.sc.ScheduledOrder.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.orderhub.sc.ScheduledOrder.domain.OrderStatus;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.name();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        return OrderStatus.valueOf(dbData);
    }
}
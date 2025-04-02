package org.orderhub.sc.scheduledorder.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.orderhub.sc.scheduledorder.domain.OrderStatus;

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
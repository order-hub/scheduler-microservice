package org.orderhub.sc.ScheduledOrder.batch;

import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ScheduledOrderItemProcessor implements ItemProcessor<ScheduledOrder, ScheduledOrder> {
    @Override
    public ScheduledOrder process(ScheduledOrder order) {
        order.markAsProcessed();
        return order;
    }
}

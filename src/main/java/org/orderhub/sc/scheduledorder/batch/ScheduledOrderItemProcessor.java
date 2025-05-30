package org.orderhub.sc.scheduledorder.batch;

import org.orderhub.sc.scheduledorder.domain.ScheduledOrder;
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

package org.orderhub.sc.scheduledorder.batch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.orderhub.sc.scheduledorder.domain.OrderStatus;
import org.orderhub.sc.scheduledorder.domain.ProcessStatus;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrder;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class ScheduledOrderItemReader implements ItemReader<ScheduledOrder> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ScheduledOrder read() throws Exception {
        if (orders == null) {
            Instant today = Instant.now();
            Instant yesterday = today.minus(1, ChronoUnit.DAYS);

            orders = new LinkedList<>(
                    em.createQuery(
                                    "SELECT o FROM ScheduledOrder o " +
                                            "WHERE o.scheduledAt BETWEEN :start AND :end " +
                                            "AND o.processStatus = :processStatus " +
                                            "AND o.status != :cancelledStatus", ScheduledOrder.class)
                            .setParameter("start", yesterday.truncatedTo(ChronoUnit.DAYS))
                            .setParameter("end", today.truncatedTo(ChronoUnit.DAYS))
                            .setParameter("processStatus", ProcessStatus.PENDING)
                            .setParameter("cancelledStatus", OrderStatus.CANCELLED)
                            .getResultList()
            );
        }

        return orders.poll();
    }

    private Queue<ScheduledOrder> orders = null;
}

package org.orderhub.sc.ScheduledOrder.repository;

import org.orderhub.sc.ScheduledOrder.domain.ProcessStatus;
import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ScheduledOrderRepository extends JpaRepository<ScheduledOrder, Long> {
    List<ScheduledOrder> findByScheduledAtBetweenAndProcessStatus(Instant start, Instant end, ProcessStatus status);
}

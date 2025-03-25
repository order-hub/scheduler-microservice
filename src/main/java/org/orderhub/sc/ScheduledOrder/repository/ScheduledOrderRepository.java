package org.orderhub.sc.ScheduledOrder.repository;

import org.orderhub.sc.ScheduledOrder.domain.ScheduledOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledOrderRepository extends JpaRepository<ScheduledOrder, Long> {
}

package org.orderhub.sc.outboxevent.respository;

import org.orderhub.sc.outboxevent.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutBoxEventRepository extends JpaRepository<OutboxEvent, Long> {
}

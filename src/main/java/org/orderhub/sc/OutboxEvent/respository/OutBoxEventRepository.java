package org.orderhub.sc.OutboxEvent.respository;

import org.orderhub.sc.OutboxEvent.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutBoxEventRepository extends JpaRepository<OutboxEvent, Long> {
}

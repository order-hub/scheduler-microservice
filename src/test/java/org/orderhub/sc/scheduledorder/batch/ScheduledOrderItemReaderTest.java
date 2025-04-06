package org.orderhub.sc.scheduledorder.batch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.orderhub.sc.scheduledorder.domain.OrderStatus;
import org.orderhub.sc.scheduledorder.domain.ProcessStatus;
import org.orderhub.sc.scheduledorder.domain.ScheduledOrder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ScheduledOrderItemReaderTest {

    private EntityManager em;
    private ScheduledOrderItemReader itemReader;

    @BeforeEach
    void setUp() {
        em = mock(EntityManager.class);
        itemReader = new ScheduledOrderItemReader();

        // @PersistenceContext 필드에 수동 주입 (Reflection으로 우회)
        try {
            var field = ScheduledOrderItemReader.class.getDeclaredField("em");
            field.setAccessible(true);
            field.set(itemReader, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReadOrdersFromEntityManagerAndReturnThemOneByOne() throws Exception {
        // Given
        ScheduledOrder order1 = mock(ScheduledOrder.class);
        ScheduledOrder order2 = mock(ScheduledOrder.class);
        List<ScheduledOrder> fakeOrders = Arrays.asList(order1, order2);

        TypedQuery query = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(ScheduledOrder.class))).thenReturn(query);
        when(query.setParameter(eq("start"), any())).thenReturn(query);
        when(query.setParameter(eq("end"), any())).thenReturn(query);
        when(query.setParameter(eq("processStatus"), eq(ProcessStatus.PENDING))).thenReturn(query);
        when(query.setParameter(eq("cancelledStatus"), eq(OrderStatus.CANCELLED))).thenReturn(query);
        when(query.getResultList()).thenReturn(fakeOrders);

        // When
        ScheduledOrder first = itemReader.read();
        ScheduledOrder second = itemReader.read();
        ScheduledOrder third = itemReader.read(); // 없을 때는 null 반환

        // Then
        assertThat(first).isSameAs(order1);
        assertThat(second).isSameAs(order2);
        assertThat(third).isNull();

        verify(em, times(1)).createQuery(anyString(), eq(ScheduledOrder.class));
    }
}

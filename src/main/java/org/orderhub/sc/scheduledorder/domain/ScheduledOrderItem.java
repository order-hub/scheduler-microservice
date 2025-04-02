package org.orderhub.sc.scheduledorder.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "scheduled_order_items")
@NoArgsConstructor
public class ScheduledOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String pricePerUnit;

    @Builder
    public ScheduledOrderItem(Long productId, String productName, String pricePerUnit, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
    }

}
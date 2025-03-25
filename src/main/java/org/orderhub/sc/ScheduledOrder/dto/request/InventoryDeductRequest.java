package org.orderhub.sc.ScheduledOrder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDeductRequest {
    private Long storeId;
    private List<InventoryItemRequest> items;
}
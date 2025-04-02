package org.orderhub.sc.scheduledorder.service.listener;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Integer price;
    private Integer quantity;
    private String imageUrl;

}

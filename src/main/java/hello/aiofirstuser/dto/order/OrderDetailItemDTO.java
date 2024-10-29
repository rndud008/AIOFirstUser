package hello.aiofirstuser.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailItemDTO {

    private Long productId;
    private Long orderItemId;
    private String productImg;
    private String productName;
    private String selectOption;
    private String combineAndQuantity;
    private boolean reviewCheck;
}

package hello.aiofirstuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderWriteResponseDTO {

    private String productImg;
    private String productName;
    private String selectOption;

    private Long cartId;
    private Long productId;
    private int quantity;

    private String combineAndQuantityPrice;
    private String point;

}

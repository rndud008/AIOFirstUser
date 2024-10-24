package hello.aiofirstuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {

    private Long productId;
    private Long productVariantId;
    private Long cartId;

    private int quantity;
    private String consumerPrice;
    private String price;
    private String totalPrice;
    private String point;

    private String productName;
    private String productImg;
    private String selectOption;
    private String delivery;

    private boolean wishItem;

}

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
    private int price;
    private int totalPrice;

    private String productName;
    private String productImg;
    private String option;
    private String delivery;

}

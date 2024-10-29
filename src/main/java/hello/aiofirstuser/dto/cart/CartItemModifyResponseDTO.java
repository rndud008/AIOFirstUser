package hello.aiofirstuser.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemModifyResponseDTO {

    private Long productVariantId;
    private Long productId;
    private Long cartId;

    private String productImg;
    private String productName;
    private String productSellPrice;

    private Map<String, Map<String,Integer>> colorSizePrice = new HashMap<>();
    private String option;
    private int quantity;
    private String combinedProductPrice;
    private String totalPrice;

}

package hello.aiofirstuser.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequestDTO {
    private Long cartId;
    private Long productId;
    private String colorSize;
    private int quantity;
}

package hello.aiofirstuser.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequestListDTO {
    List<CartRequestDTO> cartRequestDTOS;
    Long deleteCartId;
}

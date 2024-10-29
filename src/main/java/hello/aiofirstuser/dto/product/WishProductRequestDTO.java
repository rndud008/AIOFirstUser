package hello.aiofirstuser.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishProductRequestDTO {
    private Long wishProductId;
    private Long productVariantId;
}

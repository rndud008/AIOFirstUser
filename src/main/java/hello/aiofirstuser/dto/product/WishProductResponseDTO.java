package hello.aiofirstuser.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishProductResponseDTO {

    private Long wishProductId;
    private Long productId;

    private String productImg;
    private String productName;
    private String option;

    private String stock;
    private String point;
    private String combinePrice;

}

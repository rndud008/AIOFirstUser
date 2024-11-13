package hello.aiofirstuser.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailReviewDTO {
    private Long index;
    private String content;
    private String nickname;
    private String createdAt;
}

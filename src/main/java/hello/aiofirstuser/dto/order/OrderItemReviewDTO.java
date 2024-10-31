package hello.aiofirstuser.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemReviewDTO {
    private Long id;
    private String content;
    private String type;
    private Integer grade;
}

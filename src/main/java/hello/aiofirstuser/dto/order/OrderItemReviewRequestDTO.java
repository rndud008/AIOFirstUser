package hello.aiofirstuser.dto.order;

import lombok.Data;

@Data
public class OrderItemReviewRequestDTO {
    private Long orderItemId;
    private String content;
    private Integer grade;
}

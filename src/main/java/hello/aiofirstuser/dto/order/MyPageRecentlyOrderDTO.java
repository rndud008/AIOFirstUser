package hello.aiofirstuser.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageRecentlyOrderDTO {
    private Long orderId;
    private List<String> productNames;
    private String totalPrice;
    private String orderDateTime;
}

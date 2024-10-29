package hello.aiofirstuser.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private Long orderId;

    private String orderDateTime;
    private String orderStatus;
    private boolean refundCheck;

    private List<OrderDetailItemDTO> orderDetailItemDTOS;

    private String deliveryNickname;
    private List<String> phoneNumber;
    private String fullAddressName;

    private String lastTotalPrice;
    private String totalPrice;
    private String deliveryPrice;

    private String totalPoint;

}

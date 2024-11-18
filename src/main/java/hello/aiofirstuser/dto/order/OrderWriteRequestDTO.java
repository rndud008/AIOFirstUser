package hello.aiofirstuser.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderWriteRequestDTO {
    private List<Long> cartIds;
    private List<OrderProductVariantRequestDTO> orderProductVariantRequestDTOS;

    private Long addressId;
    private String deliveryPlaceStatus;
    private String deliveryMemberName;
    private String phoneNumber1;
    private String phoneNumber2;

    private String zipcode;
    private String addressName;
    private String addressNameDetail;

    private String orderMessage;
    private String bankTransferName;

    private String paymentOption;

    private String refundOption;
    private String refundMemberName;
    private String refundBankName;
    private String refundBankAccount;

}

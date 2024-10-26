package hello.aiofirstuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderWriteDeliveryResponseDTO {
    private Long addressId;

    private String deliveryMemberName = "";
    private List<String> phoneNumber1 = List.of("","","");
    private List<String> phoneNumber2 = List.of("","","");

    private String zipcode ="";
    private String addressName ="";
    private String addressNameDetail ="";
    private boolean home;

    private String orderMessage ="";
    private String bankTransferName ="";

}

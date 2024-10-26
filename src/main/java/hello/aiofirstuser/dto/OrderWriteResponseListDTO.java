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
public class OrderWriteResponseListDTO {
    List<OrderWriteResponseDTO> orderWriteResponseDTOS;
    private String totalPrice;
    private String totalPoint;
    private String totalPriceString;
    private String lastTotalPrice;
}

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
public class OrderWriteDeliveryResponseListDTO {
    List<OrderWriteDeliveryResponseDTO> orderWriteDeliveryResponseDTOS;
}

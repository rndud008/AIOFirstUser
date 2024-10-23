package hello.aiofirstuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.KotlinBeanInfoFactory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequestListDTO {
    List<CartRequestDTO> cartRequestDTOS;
    Long deleteCartId;
}

package hello.aiofirstuser.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishProductRequestListDTO {
    private List<WishProductRequestDTO> wishProductRequestDTOS;
}

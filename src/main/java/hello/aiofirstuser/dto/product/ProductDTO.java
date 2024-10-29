package hello.aiofirstuser.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String productName;
    private String size;

    private int consumerPrice;
    private int sellPrice;

    private String productImgFileNames;
}

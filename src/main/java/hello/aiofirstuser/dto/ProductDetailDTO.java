package hello.aiofirstuser.dto;

import hello.aiofirstuser.domain.ProductAlpha;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDTO {
    private Long id;
    private String productName;

    private int consumerPrice;
    private int sellPrice;
    private int point;

    private String size;

    private Map<String, Map<String,Integer>> colorSizePrice = new HashMap<>();
    private List<ProductVariantDTO> productVariantDTOS = new ArrayList<>();

    @Builder.Default
    private List<String> productImgFileNames = new ArrayList<>();
    @Builder.Default
    private List<String> productDescriptionImgFileNames = new ArrayList<>();

    @Builder.Default
    private List<String > productStatuses= new ArrayList<>();

    private List<ProductAlpha> productAlphas= new ArrayList<>();
}

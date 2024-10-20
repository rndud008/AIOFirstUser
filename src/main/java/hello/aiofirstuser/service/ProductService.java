package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Product;
import hello.aiofirstuser.domain.ProductDescriptionImg;
import hello.aiofirstuser.domain.ProductImg;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.ProductDTO;
import hello.aiofirstuser.dto.ProductDetailDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ProductService {

    ProductDetailDTO getProductDTO(Long productId);

    List<ProductDTO> getCategoryProductDTOS(Long id, boolean mainCategory);

    List<ProductDTO> getNewProductDTOS();

    default ProductDTO productDTO(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .consumerPrice(product.getConsumerPrice())
                .sellPrice(product.getSellPrice())
                .productImgFileNames(product.getProductImgs().stream().map(ProductImg::getFileName).toList().get(0))
                .build();

        return productDTO;
    }

    default ProductDetailDTO productDetailDTO(Product product){
        ProductDetailDTO productDetailDTO = ProductDetailDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .consumerPrice(product.getConsumerPrice())
                .sellPrice(product.getSellPrice())
                .productImgFileNames(product.getProductImgs().stream().map(ProductImg::getFileName).toList())
                .productDescriptionImgFileNames(product.getProductDescriptionImgs().stream().map(ProductDescriptionImg::getDescriptionFileName).toList())
                .point(product.getSellPrice()/100)
                .build();

        return productDetailDTO;
    }


    default String sizePrice(String size, Integer price){

        if(price > 0){
            return size+"(+"+price+")";
        }

        if(price < 0){
            return size+"(-"+price+")";
        }

        return size;
    }

    default Map<String,Map<String,Integer>> colorSizePrice(List<ProductVariant> productVariants){

        Map<String, Map<String,Integer>> colorSizePrice = productVariants.stream().collect(
                Collectors.groupingBy(
                        ProductVariant::getColor,
                        Collectors.toMap(
                                pv -> sizePrice(pv.getSize(),pv.getPrice()),
                                ProductVariant::getPrice
                        )
                )
        );

        return colorSizePrice;
    }

}

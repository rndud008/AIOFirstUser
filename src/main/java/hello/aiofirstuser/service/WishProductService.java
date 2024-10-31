package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.domain.WishProduct;
import hello.aiofirstuser.dto.product.WishProductRequestListDTO;
import hello.aiofirstuser.dto.product.WishProductResponseDTO;

import java.util.List;

public interface WishProductService {

    int save(Long productVariantId, Member member);

    int remove(Long wishProductId, Member member);

    int removeAll(WishProductRequestListDTO wishProductRequestListDTO, Member member);

    List<WishProductResponseDTO> getMemberWishList(Member member);

    default WishProduct entityCreate(ProductVariant productVariant, Member member){
        return WishProduct.builder()
                .productVariant(productVariant)
                .member(member)
                .build();
    }

    default WishProductResponseDTO entityToWishProductResponseDTO(WishProduct wishProduct){
        boolean stockCheck = wishProduct.getProductVariant().getStockQuantity() > 0;

        return WishProductResponseDTO.builder()
                .productId(wishProduct.getProductVariant().getProduct().getId())
                .wishProductId(wishProduct.getId())
                .productVariantId(wishProduct.getProductVariant().getId())
                .productName(wishProduct.getProductVariant().getProduct().getProductName())
                .productImg(wishProduct.getProductVariant().getProduct().getProductImgs().get(0).getFileName())
                .option("색상: " +wishProduct.getProductVariant().getColor() + "| 사이즈: " + wishProduct.getProductVariant().getSize())
                .combinePrice(String.format("%,d",wishProduct.getProductVariant().getPrice() + wishProduct.getProductVariant().getProduct().getSellPrice()))
                .point(String.format("%,d",(wishProduct.getProductVariant().getPrice() + wishProduct.getProductVariant().getProduct().getSellPrice()) / 100))
                .stock(stockCheck ? "있음" : "없음")
                .build();
    }

}

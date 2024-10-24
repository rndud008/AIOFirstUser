package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.domain.WishProduct;
import hello.aiofirstuser.dto.WishProductResponseDTO;

import java.util.List;

public interface WishProductService {

    int save(Long productVariantId, Member member);

    void remove(Long wishProductId, Long memberId);

    List<WishProductResponseDTO> getMemberWishList(Long memberId);

    default WishProduct entityCreate(ProductVariant productVariant, Member member){
        return WishProduct.builder()
                .productVariant(productVariant)
                .member(member)
                .build();
    }

}

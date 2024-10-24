package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.domain.WishProduct;
import hello.aiofirstuser.dto.WishProductResponseDTO;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.repository.ProductVariantRepository;
import hello.aiofirstuser.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishProductServiceImpl implements WishProductService {

    private final WishProductRepository wishProductRepository;
    private final ProductVariantRepository productVariantRepository;
    private final MemberRepository memberRepository;

    @Override
    public int save(Long productVariantId, Member member) {
        Optional<ProductVariant> result = productVariantRepository.findById(productVariantId);

        ProductVariant productVariant = result.orElse(null);

        if(productVariant != null){

            WishProduct wishProduct = entityCreate(productVariant,member);

            wishProductRepository.save(wishProduct);
            return 1;
        } else {
            return 0;
        }


    }

    @Override
    public void remove(Long wishProductId, Long memberId) {

        Optional<WishProduct> result = wishProductRepository.findByMemberIdAndId(wishProductId, memberId);

        WishProduct wishProduct = result.orElseThrow();

        wishProductRepository.delete(wishProduct);

    }

    @Override
    public List<WishProductResponseDTO> getMemberWishList(Long memberId) {

        return null;
    }
}

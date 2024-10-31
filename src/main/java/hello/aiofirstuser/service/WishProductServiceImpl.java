package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.domain.WishProduct;
import hello.aiofirstuser.dto.product.WishProductRequestListDTO;
import hello.aiofirstuser.dto.product.WishProductResponseDTO;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.repository.ProductVariantRepository;
import hello.aiofirstuser.repository.WishProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Transactional
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
    @Transactional
    public int remove(Long wishProductId, Member member) {

        Optional<WishProduct> result = wishProductRepository.findByMemberIdAndId(wishProductId, member.getId());

        WishProduct wishProduct = result.orElse(null);

        if(wishProduct != null){
            wishProductRepository.delete(wishProduct);
            return 1;
        }

        return 0;

    }

    @Override
    @Transactional
    public int removeAll(WishProductRequestListDTO wishProductRequestListDTO, Member member) {
        List<Long> wishIds = wishProductRequestListDTO.getWishProductRequestDTOS()
                .stream().map(wishProductRequestDTO -> wishProductRequestDTO.getWishProductId()).toList();

        List<WishProduct> wishProducts = wishProductRepository.findByIdsAndMemberId(wishIds, member.getId());
        int count = 0;

        if (!wishProducts.isEmpty()){
            for (WishProduct wishProduct : wishProducts){
                wishProductRepository.delete(wishProduct);
                count++;
            }

            if (wishProducts.size() == count ){
                return 1;
            }
        }

        return 0;
    }

    @Override
    public List<WishProductResponseDTO> getMemberWishList(Member member) {

        List<WishProduct> wishProducts = wishProductRepository.findByMemberId(member.getId());

        List<WishProductResponseDTO> wishProductResponseDTOS = new ArrayList<>();

        if (!wishProducts.isEmpty()){
            for (WishProduct wishProduct : wishProducts){
                wishProductResponseDTOS.add(entityToWishProductResponseDTO(wishProduct));
            }
        }

        return wishProductResponseDTOS;
    }
}

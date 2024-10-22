package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Cart;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.CartRequestDTO;
import hello.aiofirstuser.dto.CartRequestListDTO;
import hello.aiofirstuser.dto.CartResponseDTO;
import hello.aiofirstuser.dto.CategoryRequestDTO;
import hello.aiofirstuser.repository.CartRepository;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.repository.ProductRepository;
import hello.aiofirstuser.repository.ProductVariantRepository;
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
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void save(CartRequestDTO cartRequestDTO, Member member, ProductVariant productVariant) {

        Cart cart = cartRequestDtoToEntity(cartRequestDTO, productVariant, member);

        cartRepository.save(cart);
    }

    @Override
    public Cart findByProductValiantIAndMemberIddOfCart(Long productVariantId,Long memberId) {
        return cartRepository.findByProductVariantId(productVariantId,memberId);
    }

    @Override
    @Transactional
    public void modify(CartRequestDTO cartRequestDTO, Long cartId ) {

        Optional<Cart> result = cartRepository.findById(cartId);

        Cart cart = result.orElseThrow();
        int changeQuantity = cart.getQuantity() + cartRequestDTO.getQuantity();

        cart.changeQuantity(changeQuantity);

    }

    @Override
    public List<CartResponseDTO> getUsernameOfCartResponseList(String username) {
        List<Cart> carts = cartRepository.findByMemberUsername(username);

        List<CartResponseDTO> cartResponseDTOS = new ArrayList<>();
        for(Cart cart : carts){

            cartResponseDTOS.add(entityToCartResponseDTO(cart));
        }

        return cartResponseDTOS;
    }
}

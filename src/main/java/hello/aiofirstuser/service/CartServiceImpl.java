package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Cart;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.domain.WishProduct;
import hello.aiofirstuser.dto.cart.CartItemModifyResponseDTO;
import hello.aiofirstuser.dto.cart.CartRequestDTO;
import hello.aiofirstuser.dto.cart.CartResponseDTO;
import hello.aiofirstuser.dto.cart.CartResponseListDTO;
import hello.aiofirstuser.repository.*;
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
    private final WishProductRepository wishProductRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void save(CartRequestDTO cartRequestDTO, Member member, ProductVariant productVariant) {

        Cart cart = cartRequestDtoToEntity(cartRequestDTO, productVariant, member);

        cartRepository.save(cart);
    }

    @Override
    public Cart findByProductValiantIAndMemberIddOfCart(Long productVariantId, Long memberId) {
        return cartRepository.findByProductVariantId(productVariantId, memberId);
    }

    @Override
    @Transactional
    public void modify(CartRequestDTO cartRequestDTO, Long cartId) {

        Optional<Cart> result = cartRepository.findById(cartId);

        Cart cart = result.orElseThrow();
        int changeQuantity = cart.getQuantity() + cartRequestDTO.getQuantity();

        cart.changeQuantity(changeQuantity);

    }

    @Override
    public CartResponseListDTO getUsernameOfCartResponseList(String username) {
        List<Cart> carts = cartRepository.findByMemberUsername(username);

        List<CartResponseDTO> cartResponseDTOS = new ArrayList<>();
        Long totalPrice = 0L;
        for (Cart cart : carts) {

            Optional<WishProduct> result = wishProductRepository
                    .findByMemberIdAndProductVariantId(cart.getProductVariant().getId(), cart.getMember().getId());

            WishProduct wishProduct = result.orElse(null);
            CartResponseDTO cartResponseDTO = entityToCartResponseDTO(cart);

            if (wishProduct != null) {
                cartResponseDTO.setWishItem(true);
            } else {
                cartResponseDTO.setWishItem(false);
            }
            cartResponseDTOS.add(cartResponseDTO);

            totalPrice += (getPrice(cart) * cart.getQuantity());
        }



        CartResponseListDTO cartResponseListDTO= getCategoryResponseListDTO(cartResponseDTOS, totalPrice);

        return cartResponseListDTO;
    }

    @Override
    public Cart findByCartId(Long cartId) {
        Optional<Cart> result = cartRepository.findById(cartId);
        Cart cart = result.orElseThrow();
        return cart;
    }

    @Override
    public CartItemModifyResponseDTO getCartItemModifyDTO(Long cartId) {

        Optional<Cart> result = cartRepository.getModifyCartItem(cartId);
        Cart cart = result.orElseThrow();

        List<ProductVariant> variants = productVariantRepository.findByProductId(cart.getProductVariant().getProduct().getId());

        CartItemModifyResponseDTO cartItemModifyResponseDTO = entityToCartItemModifyResponseDTO(cart);

        cartItemModifyResponseDTO.setColorSizePrice(colorSizePrice(variants));

        return cartItemModifyResponseDTO;
    }

    @Override
    @Transactional
    public int remove(Long cartId, Long memberId) {

        Cart cart = cartRepository.findByCartIdAndMemberId(cartId, memberId);

        if (cart != null) {
            cartRepository.deleteById(cartId);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Cart findByCartIdAndMemberId(Long cartId, Long memberId) {
        return cartRepository.findByCartIdAndMemberId(cartId, memberId);
    }

    @Override
    @Transactional
    public void optionModify(CartRequestDTO cartRequestDTO, Long cartId) {
        Optional<Cart> result = cartRepository.findById(cartId);

        Cart cart = result.orElseThrow();

        cart.changeQuantity(cartRequestDTO.getQuantity());
    }

    @Override
    @Transactional
    public int allRemove(Member member) {

        List<Cart> carts = cartRepository.findByMemberUsername(member.getUsername());

        if (carts != null && !carts.isEmpty()) {
            cartRepository.deleteAll(carts);
            return 1;
        }

        return 0;

    }

    @Override
    @Transactional
    public int selectRemove(List<CartRequestDTO> cartRequestDTOS, Member member) {

        List<Long> cartIds = cartRequestDTOS.stream().map(cartRequestDTO -> cartRequestDTO.getCartId()).toList();

        List<Cart> carts = new ArrayList<>();
        if(!cartIds.isEmpty()){
            carts = cartRepository.findAllById(cartIds);
        }

        if (!carts.isEmpty()) {
            for (Cart cart : carts) {
                if (cart.getMember().getId().equals(member.getId())) {
                    cartRepository.delete(cart);
                }
            }
            return 1;
        }

        return 0;
    }

    @Override
    public CartResponseListDTO getCartIdAndQuantity(List<String> cartIdAndQuantity, Member member) {

        CartResponseListDTO cartResponseListDTO = new CartResponseListDTO();
        List<CartResponseDTO> cartResponseDTOS = new ArrayList<>();

        for(String value : cartIdAndQuantity){

            Long cartId = Long.valueOf(value.split("-")[0]);
            int quantity = Integer.parseInt(value.split("-")[1]);

            Cart cart = cartRepository.findByCartIdAndMemberId(cartId, member.getId());
            if(cart == null){
                continue;
            }

            if(cart.getQuantity() != quantity){
                cart.changeQuantity(quantity);
            }

            CartResponseDTO cartResponseDTO = entityToCartResponseDTO(cart);
            cartResponseDTOS.add(cartResponseDTO);

        }
            cartResponseListDTO.setCartResponseDTOS(cartResponseDTOS);

        return cartResponseListDTO;
    }

}

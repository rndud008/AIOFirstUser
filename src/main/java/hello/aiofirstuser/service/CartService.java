package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Cart;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.CartRequestDTO;
import hello.aiofirstuser.dto.CartRequestListDTO;
import hello.aiofirstuser.dto.CartResponseDTO;

import java.util.List;

public interface CartService {

    void save(CartRequestDTO cartRequestDTO, Member member, ProductVariant productVariant);

    Cart findByProductValiantIAndMemberIddOfCart(Long productVariantId,Long memberId);

    void modify(CartRequestDTO cartRequestDTO, Long cartId );

    List<CartResponseDTO> getUsernameOfCartResponseList(String username);

    default Cart cartRequestDtoToEntity(CartRequestDTO cartRequestDTO, ProductVariant productVariant, Member member) {
        Cart cart = Cart.builder()
                .member(member)
                .productVariant(productVariant)
                .quantity(cartRequestDTO.getQuantity())
                .build();

        return cart;
    }

    default CartResponseDTO entityToCartResponseDTO(Cart cart){

        CartResponseDTO cartResponseDTO = CartResponseDTO.builder()
                .productId(cart.getProductVariant().getProduct().getId())
                .productVariantId(cart.getProductVariant().getId())
                .cartId(cart.getId())
                .quantity(cart.getQuantity())
                .price(getPrice(cart))
                .totalPrice(getPrice(cart) * cart.getQuantity())
                .productName(cart.getProductVariant().getProduct().getProductName())
                .productImg(cart.getProductVariant().getProduct().getProductImgs().get(0).getFileName())
                .option(getOption(cart))
                .delivery("[기본조건] 배송")
                .build();

        return cartResponseDTO;
    }

    private static String getOption(Cart cart) {
        return "색상 : " + cart.getProductVariant().getColor() + ", 사이즈 : " + cart.getProductVariant().getSize() + " " + cart.getQuantity() + "개";
    }

    private static int getPrice(Cart cart) {
        return cart.getProductVariant().getProduct().getSellPrice() + cart.getProductVariant().getPrice();
    }

}

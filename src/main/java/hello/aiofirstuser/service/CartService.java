package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Cart;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.cart.CartItemModifyResponseDTO;
import hello.aiofirstuser.dto.cart.CartRequestDTO;
import hello.aiofirstuser.dto.cart.CartResponseDTO;
import hello.aiofirstuser.dto.cart.CartResponseListDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CartService {

    void save(CartRequestDTO cartRequestDTO, Member member, ProductVariant productVariant);

    Cart findByProductValiantIAndMemberIddOfCart(Long productVariantId,Long memberId);

    void modify(CartRequestDTO cartRequestDTO, Long cartId );

    CartResponseListDTO getUsernameOfCartResponseList(String username);

    Cart findByCartId(Long cartId);
    
    CartItemModifyResponseDTO getCartItemModifyDTO(Long cartId);

    int remove(Long cartId, Long memberId);
    Cart findByCartIdAndMemberId(Long cartId, Long memberId);
    void optionModify(CartRequestDTO cartRequestDTO, Long cartId );

    int allRemove(Member member);

    int selectRemove(List<CartRequestDTO> cartRequestDTOS, Member member);

    CartResponseListDTO getCartIdAndQuantity(List<String> cartIdAndQuantity, Member member);

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
                .consumerPrice(getFormat(cart.getProductVariant().getProduct().getConsumerPrice()))
                .price(getFormat(getPrice(cart)))
                .totalPrice(getFormat(getPrice(cart) * cart.getQuantity()))
                .point(getFormat(getPrice(cart) * cart.getQuantity()/100))
                .productName(cart.getProductVariant().getProduct().getProductName())
                .productImg(cart.getProductVariant().getProduct().getProductImgs().get(0).getFileName())
                .selectOption(getOption(cart))
                .delivery("[기본조건] 배송")
                .build();

        return cartResponseDTO;
    }
    
    default CartItemModifyResponseDTO entityToCartItemModifyResponseDTO(Cart cart){
        CartItemModifyResponseDTO cartItemModifyResponseDTO = CartItemModifyResponseDTO.builder()
                .productName(cart.getProductVariant().getProduct().getProductName())
                .productImg(cart.getProductVariant().getProduct().getProductImgs().get(0).getFileName())
                .productSellPrice(getFormat(cart.getProductVariant().getProduct().getSellPrice()))
                .productVariantId(cart.getProductVariant().getId())
                .productId(cart.getProductVariant().getProduct().getId())
                .cartId(cart.getId())
                .option(getColorSize(cart))
                .quantity(cart.getQuantity())
                .combinedProductPrice(getFormat(getPrice(cart)))
                .totalPrice(getFormat(getPrice(cart) * cart.getQuantity()))
                .build();

        return cartItemModifyResponseDTO;
    }

    default Map<String, Map<String,Integer>> colorSizePrice(List<ProductVariant> productVariants){

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

    default String sizePrice(String size, Integer price){

        if(price > 0){
            return size+"(+"+price+")";
        }

        if(price < 0){
            return size+"(-"+price+")";
        }

        return size;
    }
    default int getPrice(Cart cart) {
        return cart.getProductVariant().getProduct().getSellPrice() + cart.getProductVariant().getPrice();
    }

    default String getTotalString(Long totalPrice){
        String totalString =  "";

        if(totalPrice >= 50000){
            totalString = """
                    
                        <span>총 상품 금액 </span>
                        <span>%,d</span>
                        <span> = </span>
                        <span>결제 예정금액 </span>
                        <span>%,d</span>
                    
                    """.formatted(totalPrice,totalPrice);
        }else {
            totalString = """
                    
                        <span>총 상품 금액 </span>
                        <span>%,d</span>
                        <span> + </span>
                        <span>배송료</span>
                        <span>%,d</span>
                        <span> = </span>
                        <span>결제 예정금액 </span>
                        <span>%,d</span>
                    
                    """.formatted(totalPrice,3000,(totalPrice+3000));
        }

        return totalString;
    }

    default CartResponseListDTO getCategoryResponseListDTO(List<CartResponseDTO> cartResponseDTOS, Long totalPrice){
        return CartResponseListDTO.builder()
                .cartResponseDTOS(cartResponseDTOS)
                .sumTotalString(getTotalString(totalPrice))
                .build();
    }

    private static String getColorSize(Cart cart) {
        return cart.getProductVariant().getColor() + "," + cart.getProductVariant().getSize();
    }

    private static String getOption(Cart cart) {
        return "색상 : " + cart.getProductVariant().getColor() + ", 사이즈 : " + cart.getProductVariant().getSize() + " " + cart.getQuantity() + "개";
    }


    private static String getFormat(int price){
        return String.format("%,d",price);
    }

}

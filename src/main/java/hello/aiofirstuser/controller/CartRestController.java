package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Cart;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.cart.CartItemModifyResponseDTO;
import hello.aiofirstuser.dto.cart.CartRequestDTO;
import hello.aiofirstuser.dto.cart.CartRequestListDTO;
import hello.aiofirstuser.service.CartService;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.ProductService;
import hello.aiofirstuser.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
@Slf4j
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;
    private final ProductVariantService productVariantService;
    private final ProductService productService;
    private final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<?> productToCartSave(@RequestBody CartRequestListDTO cartRequestListDTO, @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        Member member = memberService.findByUsername(username);

        List<CartRequestDTO> cartRequestDTOS = cartRequestListDTO.getCartRequestDTOS();

        for (CartRequestDTO cartRequestDTO : cartRequestDTOS) {

            ProductVariant productVariant = productVariantService.getProductVariant(cartRequestDTO.getProductId(),
                    cartRequestDTO.getColorSize().split("-")[0],
                    cartRequestDTO.getColorSize().split("-")[1]);

            Cart cart = cartService.findByProductValiantIAndMemberIddOfCart(productVariant.getId(), member.getId());
            if (cart != null) {
                cartService.modify(cartRequestDTO, cart.getId());
            } else {
                cartService.save(cartRequestDTO, member, productVariant);
            }

        }

        return ResponseEntity.ok("cart save success");
    }



    @PostMapping("/optionmodify")
    public String optionModify(@RequestBody CartRequestListDTO cartRequestListDTO, @AuthenticationPrincipal UserDetails userDetails) {

        log.info("cartRequestListDTO ={}", cartRequestListDTO);

        Member member = memberService.findByUsername(userDetails.getUsername());

        optionCartItemRemove(cartRequestListDTO, member);

        optionCartNewSave(cartRequestListDTO, member);

        optionCartModify(cartRequestListDTO, member);

        return "ok";
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<?> cartItemDelete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("cartId") Long cartId){

        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = cartService.remove(cartId, member.getId());

        if (result>0){
            return ResponseEntity.ok("SUCCESS");
        }else {
            return ResponseEntity.status(404).body("RESOURCE NOT FOUND");
        }
    }

    @DeleteMapping("/delete/allcart")
    public ResponseEntity<?> cartItemAllDelete(@AuthenticationPrincipal UserDetails userDetails){

        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = cartService.allRemove(member);

        if(result >0){
            return ResponseEntity.ok("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");
    }

    @DeleteMapping("/delete/selectcart")
    public ResponseEntity<?> cartItemSelectDelete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CartRequestListDTO cartRequestListDTO){

        Member member= memberService.findByUsername(userDetails.getUsername());

        List<CartRequestDTO> cartRequestDTOS = cartRequestListDTO.getCartRequestDTOS();

        int result = cartService.selectRemove(cartRequestDTOS, member);

        if(result >0){
            return ResponseEntity.ok("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");
    }

    private void optionCartItemRemove(CartRequestListDTO cartRequestListDTO, Member member) {
        if (cartRequestListDTO.getDeleteCartId() != 0) {
            cartService.remove(cartRequestListDTO.getDeleteCartId(), member.getId());
        }
    }


    private void optionCartModify(CartRequestListDTO cartRequestListDTO, Member member) {
        CartRequestDTO cartIdDTO = cartRequestListDTO
                .getCartRequestDTOS().stream().filter(cartRequestDTO -> cartRequestDTO.getCartId() != null).findFirst().orElse(new CartRequestDTO());

        if (cartIdDTO.getCartId() == null) {
            return;
        }

        Cart cart = cartService.findByCartIdAndMemberId(cartIdDTO.getCartId(), member.getId());

        if (cart != null) {
            cartService.optionModify(cartIdDTO, cart.getId());
        }
    }


    private void optionCartNewSave(CartRequestListDTO cartRequestListDTO, Member member) {
        List<CartRequestDTO> cartIdNullList = cartRequestListDTO
                .getCartRequestDTOS().stream().filter(cartRequestDTO -> cartRequestDTO.getCartId() == null).toList();


        if (!cartIdNullList.isEmpty()) {
            for (CartRequestDTO cartRequestDTO : cartIdNullList) {
                if (cartRequestDTO.getQuantity() != 0 && cartRequestDTO.getProductId() != null && cartRequestDTO.getColorSize() != null) {
                    ProductVariant productVariant = productVariantService.getProductVariant(cartRequestDTO.getProductId(),
                            cartRequestDTO.getColorSize().split("-")[0],
                            cartRequestDTO.getColorSize().split("-")[1]);

                    Cart cart = cartService.findByProductValiantIAndMemberIddOfCart(productVariant.getId(), member.getId());
                    if (cart != null) {
                        cartService.optionModify(cartRequestDTO, cart.getId());
                    } else {
                        cartService.save(cartRequestDTO, member, productVariant);
                    }

                }
            }
        }
    }
}

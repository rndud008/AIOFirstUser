package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Cart;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.CartRequestDTO;
import hello.aiofirstuser.dto.CartRequestListDTO;
import hello.aiofirstuser.dto.MemberDTO;
import hello.aiofirstuser.service.CartService;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.ProductService;
import hello.aiofirstuser.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

            Cart cart = cartService.findByProductValiantIAndMemberIddOfCart(productVariant.getId(),member.getId());
            if (cart != null) {
                cartService.modify(cartRequestDTO,cart.getId());
            } else {
                cartService.save(cartRequestDTO, member, productVariant);
            }

        }

        return ResponseEntity.ok("cart save success");
    }
}
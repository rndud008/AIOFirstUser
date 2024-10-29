package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.product.WishProductRequestDTO;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.ProductVariantService;
import hello.aiofirstuser.service.WishProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/wish")
public class WishProductRestController {

    private final MemberService memberService;
    private final ProductVariantService productVariantService;
    private final WishProductService wishProductService;

    @PostMapping("/save")
    public ResponseEntity<?> wishProductSave(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WishProductRequestDTO wishProductRequestDTO){

        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = wishProductService.save(wishProductRequestDTO.getProductVariantId(),member);

        if(result > 0){
            return ResponseEntity.ok("SUCCESS");
        }else {
            return ResponseEntity.status(404).body("FAIL");
        }

    }

}

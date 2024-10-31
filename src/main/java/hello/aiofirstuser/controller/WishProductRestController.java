package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.product.WishProductRequestDTO;
import hello.aiofirstuser.dto.product.WishProductRequestListDTO;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.ProductVariantService;
import hello.aiofirstuser.service.WishProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/wish")
public class WishProductRestController {

    private final MemberService memberService;
    private final ProductVariantService productVariantService;
    private final WishProductService wishProductService;

    @PostMapping("/save")
    public ResponseEntity<?> wishProductSave(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WishProductRequestDTO wishProductRequestDTO) {

        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = wishProductService.save(wishProductRequestDTO.getProductVariantId(), member);

        if (result > 0) {
            return ResponseEntity.ok("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> wishProductDelete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long wishProductId) {

        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = wishProductService.remove(wishProductId, member);

        if (result > 0) {
            return ResponseEntity.ok("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");
    }

    @DeleteMapping("/delete/select")
    public ResponseEntity<?> wishProductSelectDelete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WishProductRequestListDTO wishProductRequestListDTO){
        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = wishProductService.removeAll(wishProductRequestListDTO,member);

        if (result>0){
            return ResponseEntity.ok("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");

    }




}

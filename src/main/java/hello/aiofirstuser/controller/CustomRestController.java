package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.custom.PostRequestDTO;
import hello.aiofirstuser.dto.order.OrderItemReviewDTO;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.OrderItemReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/combines")
@RequiredArgsConstructor
@Slf4j
public class CustomRestController {
    private final MemberService memberService;
    private final OrderItemReviewService orderItemReviewService;


    @GetMapping("/form")
    public ResponseEntity<?> combinesPostModifyForm(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute PostRequestDTO postRequestDTO){
        Member member = memberService.findByUsername(userDetails.getUsername());

        if (postRequestDTO.getType().equals("리뷰")){
            OrderItemReviewDTO orderItemReviewDTO = orderItemReviewService.getOrderItemReviewDTO(member,postRequestDTO);

            return ResponseEntity.ok(orderItemReviewDTO);
        }

        return null;
    }

    @PostMapping("/modify")
    public ResponseEntity<?> combinesPostModify(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PostRequestDTO postRequestDTO){
        Member member = memberService.findByUsername(userDetails.getUsername());

        int result = 0;

        if (postRequestDTO.getType().equals("리뷰")){
            result = orderItemReviewService.modify(postRequestDTO,member);

        }

        if (result > 0){
            return ResponseEntity.ok("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");
    }
}

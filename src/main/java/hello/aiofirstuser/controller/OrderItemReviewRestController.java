package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.order.OrderItemReviewRequestDTO;
import hello.aiofirstuser.service.OrderItemReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/reviews")
@Slf4j
public class OrderItemReviewRestController {

    private final OrderItemReviewService orderItemReviewService;

    @PostMapping("/save")
    public String save(@RequestBody OrderItemReviewRequestDTO orderItemReviewRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {

        int result = orderItemReviewService.save(orderItemReviewRequestDTO, userDetails.getUsername());

        if (result > 0) {
            return "SUCCESS";
        }

        return "잘못된 요청입니다";

    }
}

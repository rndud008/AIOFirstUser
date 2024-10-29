package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.kakaopay.KakaoPayReadyRequestDTO;
import hello.aiofirstuser.dto.kakaopay.KakaoPayReadyResponseDTO;
import hello.aiofirstuser.dto.order.OrderWriteRequestDTO;
import hello.aiofirstuser.service.KakaoPayService;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/order")
@Slf4j
public class OrderRestController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final KakaoPayService kakaoPayService;

    @PostMapping("/save")
    public KakaoPayReadyResponseDTO orderSave(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderWriteRequestDTO orderWriteRequestDTO){
        log.info("orderWriteRequestDTO ={}",orderWriteRequestDTO);

        KakaoPayReadyRequestDTO kakaoPayReadyRequestDTO = orderService.orderSave(userDetails.getUsername(), orderWriteRequestDTO);

        return kakaoPayService.kakaoPayReadyResponseDTO(kakaoPayReadyRequestDTO);

    }


}

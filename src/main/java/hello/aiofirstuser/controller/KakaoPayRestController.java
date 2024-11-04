package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Order;
import hello.aiofirstuser.domain.OrderStatus;
import hello.aiofirstuser.domain.PaymentStatus;
import hello.aiofirstuser.dto.kakaopay.KakaoPayApproveResponseDTO;
import hello.aiofirstuser.service.KakaoPayService;
import hello.aiofirstuser.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/kakaopay")
public class KakaoPayRestController {
    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;

    @GetMapping("/success")
    public ResponseEntity<?> successKakaoPayRequest(@RequestParam("pg_token") String pgToken, @AuthenticationPrincipal UserDetails userDetails){

        KakaoPayApproveResponseDTO kakaoPayApproveResponseDTO = kakaoPayService.kakaoPayApproveResponseDTO(pgToken, userDetails.getUsername());

        return ResponseEntity.ok(kakaoPayApproveResponseDTO);
    }

    @GetMapping("/fail")
    public void fail(@AuthenticationPrincipal UserDetails userDetails){
        kakaoPayService.paymentStatusChange(userDetails.getUsername(), PaymentStatus.FAIL);

    }

    @GetMapping("/cancel")
    public void cancel(@AuthenticationPrincipal UserDetails userDetails){
        kakaoPayService.paymentStatusChange(userDetails.getUsername(), PaymentStatus.CANCEL);

    }

    @PostMapping("/refund")
    public String refund(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("orderId") Long orderId){

        Order order = orderService.getOrderChangeStatus(orderId,userDetails.getUsername());

        if (order!= null && order.getOrderStatus().name().equals(OrderStatus.ORDER_CANCELED.name())){
            kakaoPayService.kakaoCancel(order);
            return "주문취소 완료";
        }

        if (order!= null && order.getOrderStatus().name().equals(OrderStatus.ADMIN_ITEM_CHECK.name())){

            return "관리자의 확인이 필요합니다.";
        }

        return "잘못된 접근입니다.";
    }

}

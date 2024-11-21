package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Order;
import hello.aiofirstuser.domain.OrderStatus;
import hello.aiofirstuser.domain.PaymentStatus;
import hello.aiofirstuser.dto.kakaopay.KakaoPayApproveResponseDTO;
import hello.aiofirstuser.service.KakaoPayService;
import hello.aiofirstuser.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/kakaopay")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;

    @GetMapping("/success")
    public String successKakaoPayRequest(Model model, @RequestParam("pg_token") String pgToken, @AuthenticationPrincipal UserDetails userDetails){

        KakaoPayApproveResponseDTO kakaoPayApproveResponseDTO = kakaoPayService.kakaoPayApproveResponseDTO(pgToken, userDetails.getUsername());

        Long.valueOf(kakaoPayApproveResponseDTO.getPartner_order_id());

        model.addAttribute("response", kakaoPayApproveResponseDTO);
        return "kakaoPay/approve";
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

        if (order!= null && order.isAdminCheck()){

            return "관리자의 확인이 필요합니다.";
        }

        return "잘못된 접근입니다.";
    }

}

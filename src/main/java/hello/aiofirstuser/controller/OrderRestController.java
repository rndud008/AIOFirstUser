package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.OrderWriteRequestDTO;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.OrderService;
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
@RequestMapping("/api/user/order")
@Slf4j
public class OrderRestController {
    private final MemberService memberService;
    private final OrderService orderService;

    @PostMapping("/save")
    public Long orderSave(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderWriteRequestDTO orderWriteRequestDTO){
        log.info("orderWriteRequestDTO ={}",orderWriteRequestDTO);

        return orderService.orderSave(userDetails.getUsername(), orderWriteRequestDTO);

    }
}

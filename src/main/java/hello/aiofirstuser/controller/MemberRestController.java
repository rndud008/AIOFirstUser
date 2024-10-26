package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.OrderWriteDeliveryResponseDTO;
import hello.aiofirstuser.dto.OrderWriteDeliveryResponseListDTO;
import hello.aiofirstuser.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/addresses")
    public ResponseEntity<?> getMemberAddresses(@AuthenticationPrincipal UserDetails userDetails){

        OrderWriteDeliveryResponseListDTO orderWriteDeliveryResponseListDTO
                = memberService.getOrderMemberAddresses(userDetails.getUsername());

        return ResponseEntity.ok(orderWriteDeliveryResponseListDTO);
    }

    @GetMapping("/addresses/address")
    public ResponseEntity<?> getMemberAddress(@RequestParam("status") String status, @AuthenticationPrincipal UserDetails userDetails){

        OrderWriteDeliveryResponseDTO orderWriteDeliveryResponseDTO
                = memberService.getOrderMemberAddress(userDetails.getUsername(),status);

        return ResponseEntity.ok(orderWriteDeliveryResponseDTO);
    }

    @GetMapping("/addresses/address/{id}")
    public ResponseEntity<?> getAddressId(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){

        OrderWriteDeliveryResponseDTO orderWriteDeliveryResponseDTO
                = memberService.getOrderMemberAddressId(userDetails.getUsername(), id);

        return ResponseEntity.ok(orderWriteDeliveryResponseDTO);

    }


}

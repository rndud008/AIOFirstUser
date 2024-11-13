package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.order.OrderWriteDeliveryResponseDTO;
import hello.aiofirstuser.dto.order.OrderWriteDeliveryResponseListDTO;
import hello.aiofirstuser.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        cookieReset(response);

        return ResponseEntity.status(200).body("SUCCESS");
    }

    private static void cookieReset(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken",null);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("refreshToken",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("JSESSIONID",null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


}

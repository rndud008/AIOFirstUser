package hello.aiofirstuser.controller;

import com.amazonaws.Response;
import hello.aiofirstuser.dto.KakaoPayApproveRequestDTO;
import hello.aiofirstuser.dto.KakaoPayApproveResponseDTO;
import hello.aiofirstuser.service.KakaoPayService;
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

    @GetMapping("/success")
    public ResponseEntity<?> successKakaoPayRequest(@RequestParam("pg_token") String pgToken, @AuthenticationPrincipal UserDetails userDetails){

        KakaoPayApproveResponseDTO kakaoPayApproveResponseDTO = kakaoPayService.kakaoPayApproveResponseDTO(pgToken, userDetails.getUsername());

        return ResponseEntity.ok(kakaoPayApproveResponseDTO);
    }

    @GetMapping("/fail")
    public void fail(){

    }

    @GetMapping("/cancel")
    public void cancel(){

    }
}

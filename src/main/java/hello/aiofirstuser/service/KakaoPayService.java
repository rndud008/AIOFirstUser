package hello.aiofirstuser.service;

import hello.aiofirstuser.config.KakaoPayProperties;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.Order;
import hello.aiofirstuser.domain.Payment;
import hello.aiofirstuser.dto.KakaoPayApproveResponseDTO;
import hello.aiofirstuser.dto.KakaoPayReadyRequestDTO;
import hello.aiofirstuser.dto.KakaoPayReadyResponseDTO;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.repository.OrderRepository;
import hello.aiofirstuser.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    private final KakaoPayProperties kakaoPayProperties;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    private HttpHeaders getHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "SECRET_KEY " + kakaoPayProperties.getSecretKey();
        log.info("auth={}",auth);
        httpHeaders.set("Authorization",auth);
        httpHeaders.set("Content-Type","application/json");
        return httpHeaders;
    }

    @Transactional
    public KakaoPayReadyResponseDTO kakaoPayReadyResponseDTO(KakaoPayReadyRequestDTO kakaoPayReadyRequestDTO){

        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(getParameters(kakaoPayReadyRequestDTO),this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoPayReadyResponseDTO kakaoPayReadyResponseDTO = restTemplate.postForObject(
                kakaoPayProperties.getReadyUrl(), requestEntity, KakaoPayReadyResponseDTO.class);

        Order order = orderRepository.getOrderIdAndMemberId(Long.valueOf(kakaoPayReadyRequestDTO.getPartner_order_id()), Long.valueOf(kakaoPayReadyRequestDTO.getPartner_user_id()));

        Payment payment = createPayment(order, kakaoPayReadyRequestDTO, kakaoPayReadyResponseDTO);

        paymentRepository.save(payment);

        return kakaoPayReadyResponseDTO;
    }

    @Transactional
    public KakaoPayApproveResponseDTO kakaoPayApproveResponseDTO(String pagToken, String username){

        Payment payment = paymentRepository.getWithTid(username.toUpperCase());

        HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(getApproveParameters(pagToken, payment),this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoPayApproveResponseDTO kakaoPayApproveResponseDTO = restTemplate.postForObject(
                kakaoPayProperties.getApproveUrl(), requestEntity, KakaoPayApproveResponseDTO.class);


        return kakaoPayApproveResponseDTO;
    }

    private Map<String, String> getApproveParameters(String pagToken, Payment payment) {

        Map<String,String> parameters = new HashMap<>();
        parameters.put("cid", kakaoPayProperties.getCid());
        parameters.put("tid",payment.getTid());
        parameters.put("partner_order_id", String.valueOf(payment.getOrder().getId()));
        parameters.put("partner_user_id", String.valueOf(payment.getOrder().getMember().getId()));
        parameters.put("pg_token", pagToken);

        return parameters;
    }

    private static Map<String, Object> getParameters(KakaoPayReadyRequestDTO kakaoPayReadyRequestDTO) {
        Map<String,Object> parameters = new HashMap<>();

        parameters.put("cid", kakaoPayReadyRequestDTO.getCid());
        parameters.put("partner_order_id", kakaoPayReadyRequestDTO.getPartner_order_id());
        parameters.put("partner_user_id", kakaoPayReadyRequestDTO.getPartner_user_id());
        parameters.put("item_name", kakaoPayReadyRequestDTO.getItem_name());
        parameters.put("quantity", kakaoPayReadyRequestDTO.getQuantity());
        parameters.put("total_amount", kakaoPayReadyRequestDTO.getTotal_amount());
        parameters.put("tax_free_amount", kakaoPayReadyRequestDTO.getTax_free_amount());
        parameters.put("approval_url", kakaoPayReadyRequestDTO.getApproval_url());
        parameters.put("cancel_url", kakaoPayReadyRequestDTO.getCancel_url());
        parameters.put("fail_url", kakaoPayReadyRequestDTO.getFail_url());

        return parameters;

    }

    public Payment createPayment(Order order, KakaoPayReadyRequestDTO kakaoPayReadyRequestDTO, KakaoPayReadyResponseDTO kakaoPayReadyResponseDTO){
        return Payment.builder()
                .total_amount(kakaoPayReadyRequestDTO.getTotal_amount())
                .tax_free_amount(kakaoPayReadyRequestDTO.getTax_free_amount())
                .tid(kakaoPayReadyResponseDTO.getTid())
                .order(order)
                .member(order.getMember())
                .build();
    }
}

package hello.aiofirstuser.dto.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayApproveRequestDTO {
    private String cid;
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;
}

package hello.aiofirstuser.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    private String addressName;
    private String nickname;
    private String orderMessage;
    private String bankTransferDepositor;// 무통장입금자
    private int phoneNumber1;
    private int phoneNumber2;

}

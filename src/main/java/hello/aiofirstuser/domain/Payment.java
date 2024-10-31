package hello.aiofirstuser.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
    private String tid;
    private Integer total_amount;
    private Integer tax_free_amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    public void changeStauts(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }


}

package hello.aiofirstuser.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

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

    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column(insertable = false)
    private LocalDateTime updateAt;

    public void changeStauts(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

}

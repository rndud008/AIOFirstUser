package hello.aiofirstuser.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "m_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String paymentOption;

    private String refundOption;
    private String refundMemberName;
    private String refundBankName;
    private String refundBankAccount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private boolean adminCheck;


    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void changeAdmin(boolean check){
        this.adminCheck = check;
    }


}

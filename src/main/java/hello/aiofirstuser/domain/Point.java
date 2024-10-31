package hello.aiofirstuser.domain;

import hello.aiofirstuser.repository.PointRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true)
public class Point extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private long point;
    private long currentPoint;
    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;


    public void changeValue(long value, PointStatus pointStatus){
        this.currentPoint = this.currentPoint + value;
        this.pointStatus = pointStatus;
    }



}

package hello.aiofirstuser.domain;


import hello.aiofirstuser.dto.custom.PostRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderItemReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private String content;
    private Integer grade;

    public void changeValue(PostRequestDTO postRequestDTO){
        this.content = postRequestDTO.getContent();
        this.grade = postRequestDTO.getGrade();
    }



}

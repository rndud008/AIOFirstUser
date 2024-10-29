package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.OrderItem;
import hello.aiofirstuser.domain.OrderItemReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemReviewRepository extends JpaRepository<OrderItemReview, Long> {

    @Query("select count (oir) from OrderItemReview oir " +
            "join oir.member m " +
            "where m.id = :memberId")
    int orderItemReviewCount(@Param("memberId") Long memberId);

    @Query("select oir from OrderItemReview oir " +
            "join oir.orderItem oi " +
            "where oi.id = :orderItemId")
    OrderItemReview orderItemReviewCheck(@Param("orderItemId") Long orderItemId);


}

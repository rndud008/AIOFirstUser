package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.OrderItemReview;
import hello.aiofirstuser.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemReviewRepository extends JpaRepository<OrderItemReview, Long> {

    @Query("select count (oir) from OrderItemReview oir " +
            "join oir.member m " +
            "join oir.orderItem.order o " +
            "where m.id = :memberId and o.orderStatus not in(:exCludeOrderStauts)")
    int orderItemReviewCount(@Param("memberId") Long memberId, @Param("exCludeOrderStauts")List<OrderStatus> exCludeOrderStauts);

    @Query("select oir from OrderItemReview oir " +
            "left join fetch oir.orderItem oi " +
            "where oi.id = :orderItemId")
    OrderItemReview orderItemReviewCheck(@Param("orderItemId") Long orderItemId);

    @Query("select oir from OrderItemReview oir " +
            "left join fetch oir.member " +
            "where oir.member.id = :memberId")
    List<OrderItemReview> findByMemberId(@Param("memberId") Long memberId);

    @Query("select oir from OrderItemReview oir " +
            "left join fetch oir.member " +
            "where oir.member.id = :memberId and oir.id = :orderItemReviewId")
    OrderItemReview findByMemberIdAndId(@Param("memberId") Long memberId, @Param("orderItemReviewId") Long orderItemReviewId);

}

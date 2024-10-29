package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o " +
            "left join fetch o.address " +
            "left join fetch o.address.member " +
            "where o.id = :OrderId and o.address.member.id = :MemberId")
    Order getOrderIdAndMemberId(@Param("OrderId") Long OrderId,@Param("MemberId") Long MemberId);

    @Query("select o from Order o " +
            "left join fetch o.address " +
            "left join fetch o.address.member " +
            "where o.id = :OrderId and o.address.member.username = :MemberUsername")
    Order getOrderIdAndMemberUsername(@Param("OrderId") Long OrderId,@Param("MemberUsername") String MemberUsername);

    @Query("select o from Order o " +
            "left join fetch o.address " +
            "left join fetch o.address.member " +
            "where o.address.member.id = :memberId " +
            "order by o.createdAt desc limit 10")
    List<Order> getRecentlyOrderListTop10(@Param("memberId") Long memberId);

}

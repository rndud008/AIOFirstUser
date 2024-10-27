package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o left join fetch o.member where o.id = :OrderId and o.member.id = :MemberId")
    Order getOrderIdAndMemberId(@Param("OrderId") Long OrderId,@Param("MemberId") Long MemberId);

}

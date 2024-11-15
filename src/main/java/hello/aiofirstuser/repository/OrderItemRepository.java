package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.OrderItem;
import hello.aiofirstuser.domain.OrderStatus;
import hello.aiofirstuser.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    @Query("select count(oi) from OrderItem oi " +
            "join  oi.order o " +
            "join  o.address.member m " +
            "where m.id = :memberId " +
            "and o.orderStatus not in(:exCludeOrderStatus) " +
            "and o.adminCheck = false ")
    int orderItemCount(@Param("memberId") Long memberId, @Param("exCludeOrderStatus")List<OrderStatus> exCludeOrderStatus);

    @Query(value = "select oi from OrderItem oi " +
            "left join fetch oi.order " +
            "left join fetch oi.productVariant " +
            "left join fetch oi.productVariant.product " +
            "where oi.order.id = :orderId ")
    List<OrderItem> getOrderItemList(@Param("orderId") Long orderId);

    @Query(value = "select oi.productVariant.product , count(oi.productVariant.product.id) as productCount " +
            "from OrderItem oi " +
            " join  oi.order " +
            " join  oi.productVariant " +
            " join  oi.productVariant.product " +
            "where oi.order.orderStatus = 'DELIVERY_COMPLETED' " +
            "group by oi.productVariant.product " +
            "order by count(oi.productVariant.product.id) desc ")
    List<Object[]> getProductAndCount();

    @Query("select oi from OrderItem oi " +
            "left join fetch oi.order " +
            "left join fetch oi.order.address " +
            "left join fetch oi.order.address.member " +
            "where oi.order.address.member.username =:username and oi.id = :orderItemId")
    Optional<OrderItem> getOrderItemByIdAndMemberUsername(@Param("username") String username,@Param("orderItemId") Long orderItemId);

}

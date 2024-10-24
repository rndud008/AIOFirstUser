package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c left join fetch c.productVariant where c.productVariant.id = :productVariantId " +
            "and c.member.id = :memberId")
    Cart findByProductVariantId(@Param("productVariantId") Long productVariantId, @Param("memberId") Long memberId);

    @Query("select c from Cart c left join fetch c.member where c.member.id = :memberId and c.id = :cartId")
    Cart findByCartIdAndMemberId(@Param("cartId")Long cartId, @Param("memberId")Long memberId);

    @Query("select distinct c from Cart c " +
            "left join fetch c.member " +
            "left join fetch c.productVariant " +
            "left join fetch c.productVariant.product " +
            "left join fetch c.productVariant.product.productImgs where c.member.username = :username")
    List<Cart> findByMemberUsername(@Param("username") String username);

    @Query("select c from Cart c " +
            "left join fetch c.member " +
            "left join fetch c.productVariant " +
            "left join fetch c.productVariant.product " +
            "left join fetch c.productVariant.product.productImgs " +
            "where c.id = :cartId")
    Optional<Cart> getModifyCartItem(@Param("cartId") Long cartId);

}

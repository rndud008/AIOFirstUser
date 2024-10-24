package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.WishProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishProductRepository extends JpaRepository<WishProduct,Long> {

    @Query("select w from WishProduct w " +
            "left join fetch w.member " +
            "left join fetch w.productVariant " +
            "where w.id = :wishProductId and w.member.id = :memberId")
    Optional<WishProduct> findByMemberIdAndId(@Param("wishProductId") Long wishProductId, @Param("memberId") Long memberId);

    @Query("select w from WishProduct w " +
            "left join fetch w.member " +
            "left join fetch w.productVariant " +
            "where w.productVariant.id = :productVariantId and w.member.id = :memberId")
    Optional<WishProduct> findByMemberIdAndProductVariantId(@Param("productVariantId")Long productVariantId, @Param("memberId") Long memberId);
}

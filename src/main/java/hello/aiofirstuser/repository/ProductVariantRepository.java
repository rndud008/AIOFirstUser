package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    List<ProductVariant> findByProductId(Long productId);

    @Query("select pv from ProductVariant pv " +
            "left join fetch pv.product " +
            "where pv.product.id = :productId " +
            "and pv.color = :color " +
            "and pv.size = :size")
    ProductVariant findByProductIdAndColorAndSize(@Param("productId") Long productId,@Param("color") String color,@Param("size") String size);

    @Query("select pv from ProductVariant pv " +
            "left join fetch pv.product " +
            "where pv.id in (:productVariantIds)")
    List<ProductVariant> getProductVariants(@Param("productVariantIds") List<Long> productVariantIds);
}

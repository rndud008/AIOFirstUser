package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    List<ProductVariant> findByProductId(Long productId);
}

package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryDepNo(Long categoryDepno);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByCreatedAtAfter(LocalDateTime localDateTime);
    @Query("select p from Product p " +
            "where p.productName like concat('%' ,:search ,'%') ")
    List<Product> getProducts(@Param("search") String search);
}

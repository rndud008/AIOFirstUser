package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Query("select oi.productVariant.product, count(oi.productVariant.product.id) as productCount from OrderItem oi " +
            "join oi.order " +
            "join oi.productVariant " +
            "join oi.productVariant.product " +
            "where oi.order.orderStatus = 'DELIVERY_COMPLETED' " +
            "and oi.order.createdAt between :oneWeekAgo and :today " +
            "group by oi.productVariant.product " +
            "order by count(oi.productVariant.product.id) desc ")
    List<Object[]> getWeeklyProducts(@Param("oneWeekAgo") LocalDateTime oneWeekAgo, @Param("today") LocalDateTime today, Pageable pageable);

    @Query("select p from Product p " +
            "left join fetch p.category " +
            "where p.category.depNo =:decode")
    List<Product> getDepNoFilterProducts(@Param("decode")Long decode, Sort sort);

    @Query("select p from Product p " +
            "left join fetch p.category " +
            "where p.category.id =:code")
    List<Product> getCodeFilterProducts(@Param("code")Long code, Sort sort);

}

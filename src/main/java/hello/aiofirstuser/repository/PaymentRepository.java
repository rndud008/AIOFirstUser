package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query("select p from Payment p " +
            "left join fetch p.member " +
            "left join fetch p.order " +
            "where p.member.username = :username " +
            "order by p.createdAt desc limit 1")
    Payment getWithTid(@Param("username") String username);

    Payment findByOrderId(Long orderId);
}

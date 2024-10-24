package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

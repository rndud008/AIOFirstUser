package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("select p from Point p " +
            "left join fetch p.member " +
            "where p.member.id = :meberId " +
            "order by p.createdAt desc limit 1")
    Optional<Point> findCurrentPointByMemberId(@Param("meberId") Long memberId);

    List<Point> findByMemberId(Long memberId);
}

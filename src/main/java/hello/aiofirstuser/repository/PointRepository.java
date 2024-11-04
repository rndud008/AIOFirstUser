package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Point;
import hello.aiofirstuser.domain.PointStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("select p from Point p " +
            "left join fetch p.member " +
            "where p.member.id = :memberId " +
            "order by p.createdAt desc limit 1")
    Optional<Point> findCurrentPointByMemberId(@Param("memberId") Long memberId);

    @Query("select p from Point p " +
            "left join fetch p.member " +
            "where p.member.id = :memberId " +
            "order by p.createdAt desc ")
    List<Point> findByMemberId(@Param("memberId")Long memberId);

    @Query("select p from Point p " +
            "left join fetch p.member " +
            "where p.member.id = :memberId " +
            "and p.pointStatus in(:pointStatuses)")
    List<Point> getStatusList(@Param("memberId")Long memberId, List<PointStatus> pointStatuses);
}

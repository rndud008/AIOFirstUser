package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository  extends JpaRepository<Member,Long> {

    @Query("select m from Member m left join fetch m.roles where m.username = :username")
    Member getWithRoles(@Param("username") String username);

    Member findByUsername(String username);

    boolean existsByUsername(String username);
}

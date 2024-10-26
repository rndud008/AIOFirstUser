package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.AddressStatus;
import hello.aiofirstuser.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository  extends JpaRepository<Member,Long> {

    @Query("select m from Member m left join fetch m.roles where m.username = :username")
    Member getWithRoles(@Param("username") String username);

    @Query("select m from Member m left join fetch m.addresses a where m.username = :username")
    Member getWithAddresses(@Param("username") String username);

    @Query("select m from Member m " +
            "left join fetch m.addresses a " +
            "where m.username = :username and a.addressStatus = :status")
    Member getWithAddress(@Param("username") String username, @Param("status") AddressStatus addressStatus);

    @Query("select m from Member m left join fetch m.addresses a " +
            "where m.username = :username and a.id = :addressId")
    Member getWithAddressId(@Param("username") String username, @Param("addressId") Long addressId);

    Member findByUsername(String username);

    boolean existsByUsername(String username);
}

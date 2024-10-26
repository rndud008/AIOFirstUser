package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Address;
import hello.aiofirstuser.domain.AddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query("select a from Address a " +
            "left join fetch a.member " +
            "where a.member.id = :memberId and a.addressStatus = :status")
    Address findByMemberAddress(@Param("memberId") Long memberId, @Param("status") AddressStatus addressStatus);


}

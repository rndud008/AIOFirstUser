package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

    @Query("select i from Inquiry i " +
            "left join fetch i.category " +
            "left join fetch i.product " +
            "where i.category.id = :categoryId " +
            "order by i.createdAt desc ")
    List<Inquiry> getInquiryList(@Param("categoryId") Long categoryId);


    @Query("select i from Inquiry i " +
            "left join fetch i.category " +
            "where i.id = :inquiryId")
    Inquiry getInquiry(@Param("inquiryId") Long inquiryId);
    @Query(value = "select i from Inquiry i " +
            "left join fetch i.category " +
            "where i.id = :inquiryId and i.name = :name" )
    Inquiry getInquiry(@Param("inquiryId") Long inquiryId, @Param("name") String  name);
}

package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.InquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer,Long> {

    @Query("select ia from InquiryAnswer ia " +
            "left join fetch ia.inquiry " +
            "where ia.inquiry.id = :inquiryId " +
            "order by ia.createdAt desc")
    InquiryAnswer getInquiryAnswer(Long inquiryId);

    @Query("select ia from InquiryAnswer ia " +
            "left join fetch ia.inquiry " +
            "where ia.inquiry.id in (:inquiryIds) ")
    List<InquiryAnswer> getInquiryAnswers(List<Long> inquiryIds);
}

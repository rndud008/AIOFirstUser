package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Category;
import hello.aiofirstuser.domain.Inquiry;
import hello.aiofirstuser.domain.InquiryAnswer;
import hello.aiofirstuser.dto.inquiry.InquiryCheckResponseDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDetailRequestDTO;
import hello.aiofirstuser.dto.inquiry.InquiryRequestDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface InquiryService {

    List<InquiryDTO> getInquiryDTOList(Long categoryId);
    List<InquiryDTO> getProductInquiryDTOList(Long productId);

    int save(InquiryRequestDTO inquiryRequestDTO);

    InquiryCheckResponseDTO getInquiryCheckResponseDTO(Long inquiryId);

    InquiryDTO getInquiryDTO(InquiryDetailRequestDTO inquiryDetailRequestDTO,boolean admin);

    InquiryDTO modifyInquiryDTO(InquiryRequestDTO inquiryRequestDTO);

    default InquiryCheckResponseDTO entityToInquiryCheckResponseDTO(Inquiry inquiry){
        return InquiryCheckResponseDTO.builder()
                .inquiryId(inquiry.getId())
                .categoryId(inquiry.getCategory().getId())
                .depno(inquiry.getCategory().getDepNo())
                .categoryName(inquiry.getCategory().getCategoryName())
                .build();
    }
    default InquiryDTO inquiryToInquiryDTO(Inquiry inquiry,Long index){

        return InquiryDTO.builder()
                .inquiryId(inquiry.getId())
                .productId(inquiry.getProduct() == null ? null : inquiry.getProduct().getId())
                .categoryId(inquiry.getCategory().getId())
                .categoryName(inquiry.getCategory().getCategoryName())
                .name(inquiry.getName())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .img(inquiry.getImg())
                .createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .index(index)
                .answer(inquiry.isAnswer())
                .build();
    }

    default InquiryDTO inquiryAnswerToInquiryDTO(InquiryAnswer inquiryAnswer, Long index){

        return InquiryDTO.builder()
                .inquiryId(inquiryAnswer.getInquiry().getId())
                .productId(inquiryAnswer.getInquiry().getProduct() == null ? null : inquiryAnswer.getInquiry().getProduct().getId())
                .categoryId(inquiryAnswer.getInquiry().getCategory().getId())
                .name(inquiryAnswer.getName())
                .title("->"+inquiryAnswer.getInquiry().getTitle())
                .content(inquiryAnswer.getInquiry().getContent())
                .img(inquiryAnswer.getInquiry().getImg())
                .createdAt(inquiryAnswer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .index(index)
                .answer(inquiryAnswer.getInquiry().isAnswer())
                .adminContent(inquiryAnswer.getContent())
                .build();
    }

    default Inquiry inquiryRequestDtoToEntity(InquiryRequestDTO inquiryRequestDTO, Category category){
        return Inquiry.builder()
                .category(category)
                .name(inquiryRequestDTO.getName())
                .title(inquiryRequestDTO.getTitle())
                .content(inquiryRequestDTO.getContent())
                .password(inquiryRequestDTO.getPassword())
                .build();
    }


}

package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Category;
import hello.aiofirstuser.domain.Inquiry;
import hello.aiofirstuser.dto.category.CategoryRequestDTO;
import hello.aiofirstuser.dto.inquiry.InquiryCheckResponseDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDetailRequestDTO;
import hello.aiofirstuser.dto.inquiry.InquiryRequestDTO;
import hello.aiofirstuser.repository.CategoryRepository;
import hello.aiofirstuser.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryServiceImpl implements InquiryService {
    private final CategoryRepository categoryRepository;
    private final InquiryRepository inquiryRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<InquiryDTO> getInquiryDTOList(Long categoryId) {
        List<Inquiry> inquiries = inquiryRepository.getInquiryList(categoryId);
        List<InquiryDTO> inquiryDTOS = new ArrayList<>();

        if(!inquiries.isEmpty()){
            long index = inquiries.size();
            for (Inquiry inquiry : inquiries){
                inquiryDTOS.add(entityToInquiryDTO(inquiry,index));
                index --;
            }
        }

        return inquiryDTOS;
    }

    @Override
    @Transactional
    public int save(InquiryRequestDTO inquiryRequestDTO) {
        Category category = categoryRepository.findById(inquiryRequestDTO.getCategoryId()).orElse(null);
        Category parentCategory = categoryRepository.findByCategoryName("고객센터").orElse(null);

        if(category == null || parentCategory == null || category.getDepNo() != parentCategory.getId() ){
            return 0;
        }

        inquiryRequestDTO.setPassword(passwordEncoder.encode(inquiryRequestDTO.getPassword()));
        inquiryRepository.save(inquiryRequestDtoToEntity(inquiryRequestDTO,category));

        return 1;
    }

    @Override
    public InquiryCheckResponseDTO getInquiryCheckResponseDTO(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if(inquiry.getId() == null){
            return new InquiryCheckResponseDTO();
        }

        return entityToInquiryCheckResponseDTO(inquiry);
    }

    @Override
    public InquiryDTO getInquiryDTO(InquiryDetailRequestDTO inquiryDetailRequestDTO) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryDetailRequestDTO.getInquiryId());

        if(passwordEncoder.matches(inquiryDetailRequestDTO.getPassword(),inquiry.getPassword())){
            return entityToInquiryDTO(inquiry,0L);
        }

        return new InquiryDTO();
    }

    @Override
    @Transactional
    public InquiryDTO modifyInquiryDTO(InquiryRequestDTO inquiryRequestDTO) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryRequestDTO.getInquiryId(), inquiryRequestDTO.getName());

        if(inquiry == null){
            return new InquiryDTO();
        }
        inquiry.changeValue(inquiryRequestDTO);


        return entityToInquiryDTO(inquiry, 0L);
    }
}

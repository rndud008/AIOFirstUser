package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.inquiry.InquiryDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDetailRequestDTO;
import hello.aiofirstuser.dto.inquiry.InquiryRequestDTO;
import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
@Slf4j
public class InquiryRestController {

    private final InquiryService inquiryService;
    private final CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?> inquirySave(@RequestBody InquiryRequestDTO inquiryRequestDTO){

        int result = inquiryService.save(inquiryRequestDTO);
        if(result > 0){
            return ResponseEntity.status(200).body(categoryService.getCategory(inquiryRequestDTO.getCategoryId()));
        }
        return ResponseEntity.status(404).body("FAIL");
    }

    @PostMapping("/detail")
    public ResponseEntity<?> inquiryDetail(@RequestBody InquiryDetailRequestDTO inquiryDetailRequestDTO){

        InquiryDTO inquiryDTO = inquiryService.getInquiryDTO(inquiryDetailRequestDTO);

        if(inquiryDTO.getInquiryId() == null){
            return ResponseEntity.status(404).body("FAIL");
        }

        return ResponseEntity.status(200).body(inquiryDTO);
    }

    @PostMapping("/modify")
    public ResponseEntity<?> inquiryModify(@RequestBody InquiryRequestDTO inquiryRequestDTO){

        InquiryDTO inquiryDTO =inquiryService.modifyInquiryDTO(inquiryRequestDTO);
        if (inquiryDTO.getInquiryId() == null){
            return ResponseEntity.status(404).body("FAIL");
        }

        return ResponseEntity.status(200).body(inquiryDTO);
    }

}

package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.inquiry.InquiryDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDetailRequestDTO;
import hello.aiofirstuser.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiryanswer")
@Slf4j
public class InquiryAnswerRestController {
    private final InquiryService inquiryService;


    @PostMapping("/detail")
    public ResponseEntity<?> inquiryDetail(@RequestBody InquiryDetailRequestDTO inquiryDetailRequestDTO){

        InquiryDTO inquiryDTO = inquiryService.getInquiryDTO(inquiryDetailRequestDTO,true);

        if(inquiryDTO.getInquiryId() == null){
            return ResponseEntity.status(404).body("FAIL");
        }

        return ResponseEntity.status(200).body(inquiryDTO);
    }
}

package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.inquiry.InquiryCheckResponseDTO;
import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/inquiryanswer")
public class InquiryAnswerController {
    private final CategoryService categoryService;
    private final InquiryService inquiryService;

    @GetMapping("/detail/{inquiryId}")
    public String detailPageCheckForm(@PathVariable("inquiryId") Long inquiryId, Model model){
        model.addAttribute("checkInquiryAnswer",true);
        model.addAttribute("mainCategory", categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory", categoryService.InqueryCategory());

        InquiryCheckResponseDTO inquiryCheckResponseDTO = inquiryService.getInquiryCheckResponseDTO(inquiryId);
        if (inquiryCheckResponseDTO.getInquiryId() == null){
            return "fragments/home";
        }

        model.addAttribute("inquiry",inquiryCheckResponseDTO) ;

        return "fragments/inquiry";
    }
}

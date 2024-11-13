package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Category;
import hello.aiofirstuser.domain.Inquiry;
import hello.aiofirstuser.dto.category.CategoryDTO;
import hello.aiofirstuser.dto.category.CategoryRequestDTO;
import hello.aiofirstuser.dto.inquiry.InquiryCheckResponseDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDTO;
import hello.aiofirstuser.dto.inquiry.InquiryDetailRequestDTO;
import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.InquiryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
@Slf4j
public class InquiryController {

    private final CategoryService categoryService;
    private final InquiryService inquiryService;

    @GetMapping("")
    public String inqueryPage(@ModelAttribute CategoryRequestDTO categoryRequestDTO, Model model) {
        model.addAttribute("mainCategory", categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory", categoryService.InqueryCategory());

        if (categoryRequestDTO.getCategoryName().equals("고객센터")) {

            if (categoryRequestDTO.getDecode() == null) {
                CategoryDTO categoryDTO = categoryService.getCategory(categoryRequestDTO.getCode());
                if (categoryDTO.getId() == null) {
                    return "fragments/home";
                }
                model.addAttribute("productCheck", true);
                model.addAttribute("subCategoryList", categoryService.getSubCategoryList(categoryDTO.getId()));
                categoryDTO = categoryService.getCategory("상품문의");
                model.addAttribute("inquiryList", inquiryService.getInquiryDTOList(categoryDTO.getId()));
                model.addAttribute("categoryDTO", categoryDTO);

            } else {

                CategoryDTO categoryDTO = categoryService.getCategory(categoryRequestDTO.getDecode());
                if (categoryDTO.getId() == null) {
                    return "fragments/home";
                }
                model.addAttribute("subCategoryList", categoryService.getSubCategoryList(categoryDTO.getId()));

                categoryDTO = categoryService.getCategory(categoryRequestDTO.getCode());
                if (categoryDTO.getId() == null) {
                    return "fragments/home";
                }

                if (categoryDTO.getCategoryName().equals("상품문의")) {
                    model.addAttribute("productCheck", true);
                } else if (categoryDTO.getCategoryName().equals("배송문의")) {

                } else if (categoryDTO.getCategoryName().equals("배송 전 취소/변경")) {

                } else if (categoryDTO.getCategoryName().equals("입금/기타문의")) {

                } else if (categoryDTO.getCategoryName().equals("FAQ")) {

                }

                model.addAttribute("categoryDTO", categoryDTO);
                model.addAttribute("inquiryList", inquiryService.getInquiryDTOList(categoryDTO.getId()));

            }

        } else if (categoryRequestDTO.getCategoryName().equals("공지사항")) {

        } else if (categoryRequestDTO.getCategoryName().equals("상품리뷰")) {

        } else {
            return "fragments/home";
        }

        return "fragments/inquiry";
    }

    @GetMapping("/saveform")
    public String inquirySaveForm(@ModelAttribute CategoryRequestDTO categoryRequestDTO, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        String name;

        if (userDetails == null || userDetails.getUsername() == null){
            name = "aaa";
        }else {
            name = userDetails.getUsername() ;
        }

        CategoryDTO categoryDTO = categoryService.getCategory(categoryRequestDTO.getCode());

        if (categoryDTO.getCategoryName().equals("상품문의")) {
            InquiryDTO inquiryDTO = new InquiryDTO();
            inquiryDTO.setName(name);
            model.addAttribute("productSaveForm", inquiryDTO);
            model.addAttribute("productSaveFormCheck", true);
        }

        model.addAttribute("categoryDTO", categoryDTO);
        model.addAttribute("mainCategory", categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory", categoryService.InqueryCategory());

        return "fragments/inquiry";
    }

    @GetMapping("/detail/{inquiryId}")
    public String detailPageCheckForm(@PathVariable("inquiryId") Long inquiryId,Model model){
        model.addAttribute("checkInquiry",true);
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

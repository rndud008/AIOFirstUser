package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.category.CategoryRequestDTO;
import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.InquiryService;
import hello.aiofirstuser.service.OrderItemReviewService;
import hello.aiofirstuser.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/product")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderItemReviewService orderItemReviewService;
    private final InquiryService inquiryService;

    @GetMapping("/category")
    public String productList(@ModelAttribute CategoryRequestDTO categoryRequestDTO, Model model){

        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        if (categoryRequestDTO.getDecode() == null){
            model.addAttribute("category", categoryService.getCategory(categoryRequestDTO.getCode()));
            model.addAttribute("newProducts", productService.getCategoryProductDTOS(categoryRequestDTO.getCode(),true));
        }else {
            model.addAttribute("category", categoryService.getCategory(categoryRequestDTO.getDecode()));
            model.addAttribute("newProducts", productService.getCategoryProductDTOS(categoryRequestDTO.getCode(),false));
        }

        return "fragments/productList";
    }

    @GetMapping("/search")
    public String productSearchList(Model model, @RequestParam(required = false) String search){
        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        model.addAttribute("products",productService.getSearchProductDTOS(search));

        return "fragments/productSearchList";
    }

    @GetMapping("/detail/{productId}")
    public String productDetail(@PathVariable("productId") Long productId,Model model){
        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        model.addAttribute("product", productService.getProductDTO(productId));
        model.addAttribute("productReviews",orderItemReviewService.getProductDetailReviewDTOS(productId));
        model.addAttribute("productInquiries",inquiryService.getProductInquiryDTOList(productId));

        return "fragments/productDetail";
    }

}

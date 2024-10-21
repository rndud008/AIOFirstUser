package hello.aiofirstuser.controller;

import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model){

        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        model.addAttribute("newProducts",productService.getNewProductDTOS());

        return "fragments/home";
    }


}

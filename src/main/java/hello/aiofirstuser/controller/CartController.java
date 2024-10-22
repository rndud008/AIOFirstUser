package hello.aiofirstuser.controller;

import hello.aiofirstuser.service.CartService;
import hello.aiofirstuser.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/cart")
public class CartController {

    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping("")
    public String cartPage(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        model.addAttribute("cartList",cartService.getUsernameOfCartResponseList(userDetails.getUsername()));

        return "fragments/cart";
    }
}
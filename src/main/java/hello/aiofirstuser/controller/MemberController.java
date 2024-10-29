package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.OrderService;
import hello.aiofirstuser.service.WishProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class MemberController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final WishProductService wishProductService;
    private final CategoryService categoryService;

    @GetMapping("/mypage")
    public String mpPage(@RequestParam(required = false, name = "mypagetype") String myPageType, Model model, @AuthenticationPrincipal UserDetails userDetails){

        Member member = memberService.findByUsername(userDetails.getUsername());

        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        if(myPageType == null){
            model.addAttribute("member",memberService.getMyPageMemberDTO(member));
            model.addAttribute("orders",orderService.getMyPageRecentlyOrderDTO(member));

        } else if(myPageType.equals("orderList")){

        } else if(myPageType.equals("wishList")){

        } else if(myPageType.equals("reviewList")){

        } else if(myPageType.equals("pointList")){

        }

        return "fragments/mypage";
    }

}

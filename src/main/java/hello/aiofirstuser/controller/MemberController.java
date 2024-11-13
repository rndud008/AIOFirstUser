package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.service.*;
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
    private final PointService pointService;
    private final OrderItemReviewService orderItemReviewService;

    @GetMapping("/mypage")
    public String mpPage(@RequestParam(required = false, name = "mypagetype") String myPageType, Model model, @AuthenticationPrincipal UserDetails userDetails){

        Member member = memberService.findByUsername(userDetails.getUsername());

        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        if(myPageType == null){
            model.addAttribute("name","마이페이지");
            model.addAttribute("myPageCheck",true);
            model.addAttribute("member",memberService.getMyPageMemberDTO(member));
            model.addAttribute("orders",orderService.getOrderList(member,true));

        } else if(myPageType.equals("orderList")){
            model.addAttribute("name","주문내역");
            model.addAttribute("orderCheck",true);
            model.addAttribute("member",memberService.entityToMemberDTO(member));
            model.addAttribute("orders",orderService.getOrderList(member,false));

        } else if(myPageType.equals("wishList")){
            model.addAttribute("name","관심상품");
            model.addAttribute("wishCheck",true);
            model.addAttribute("member",memberService.entityToMemberDTO(member));
            model.addAttribute("wishList",wishProductService.getMemberWishList(member));

        } else if(myPageType.equals("postList")){
            model.addAttribute("name","내 게시글 목록");
            model.addAttribute("postCheck",true);
            model.addAttribute("member",memberService.entityToMemberDTO(member));
            model.addAttribute("postList",orderItemReviewService.getPostResponseDTOList(member));

        } else if(myPageType.equals("pointList")){
            model.addAttribute("name","적립금내역");
            model.addAttribute("pointCheck",true);
            model.addAttribute("member",memberService.entityToMemberDTO(member));
            model.addAttribute("pointList",pointService.getMyPagePointDTOS(member));
        }

        return "fragments/mypage";
    }

}

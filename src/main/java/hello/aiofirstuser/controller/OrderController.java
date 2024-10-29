package hello.aiofirstuser.controller;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.service.CartService;
import hello.aiofirstuser.service.CategoryService;
import hello.aiofirstuser.service.MemberService;
import hello.aiofirstuser.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CategoryService categoryService;
    private final CartService cartService;
    private final MemberService memberService;
    private final OrderService orderService;



    @GetMapping("")
    public String orderPage(@RequestParam List<String> cartIdAndQuantity, @AuthenticationPrincipal UserDetails userDetails, Model model){

        log.info("orderPage cartIdAndQuantity={}",cartIdAndQuantity);

        Member member = memberService.findByUsername(userDetails.getUsername());

        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        model.addAttribute("cartItemList",orderService.orderWriteResponseList(cartIdAndQuantity,member));
        model.addAttribute("orderMember",memberService.getOrderMember(member.getUsername()));

        return "fragments/order";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetailPage(@PathVariable("orderId") Long orderId, Model model,@AuthenticationPrincipal UserDetails userDetails){

        Member member = memberService.findByUsername(userDetails.getUsername());

        model.addAttribute("mainCategory",categoryService.mainCategoryInqueryExcludeList());
        model.addAttribute("inqueryCategory",categoryService.InqueryCategory());

        model.addAttribute("orderDetail",orderService.getOrderDetailDTO(orderId,member));

        return "fragments/orderDetail";
    }

}

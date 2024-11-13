package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.OrderItem;
import hello.aiofirstuser.domain.OrderItemReview;
import hello.aiofirstuser.dto.custom.PostRequestDTO;
import hello.aiofirstuser.dto.custom.PostResponseDTO;
import hello.aiofirstuser.dto.order.OrderItemReviewDTO;
import hello.aiofirstuser.dto.order.OrderItemReviewRequestDTO;
import hello.aiofirstuser.dto.product.ProductDetailReviewDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface OrderItemReviewService {
    int save(OrderItemReviewRequestDTO orderItemReviewRequestDTO, String username);

    int modify(PostRequestDTO postRequestDTO, Member member);

    List<PostResponseDTO> getPostResponseDTOList(Member member);

    OrderItemReviewDTO getOrderItemReviewDTO(Member member, PostRequestDTO postRequestDTO);

    List<ProductDetailReviewDTO> getProductDetailReviewDTOS(Long productId);

    default OrderItemReviewDTO entityToOrderItemReviewDTO(OrderItemReview orderItemReview){
        return OrderItemReviewDTO.builder()
                .id(orderItemReview.getId())
                .type("리뷰")
                .content(orderItemReview.getContent())
                .grade(orderItemReview.getGrade())
                .build();
    }

    default OrderItemReview dtoToEntity(OrderItemReviewRequestDTO orderItemReviewRequestDTO, OrderItem orderItem){
       return OrderItemReview.builder()
                .grade(orderItemReviewRequestDTO.getGrade())
                .content(orderItemReviewRequestDTO.getContent())
                .member(orderItem.getOrder().getAddress().getMember())
                .orderItem(orderItem)
                .build();
    }

    default PostResponseDTO entityToPostResponseDTO(OrderItemReview orderItemReview,String str,long index){
        return PostResponseDTO.builder()
                .id(orderItemReview.getId())
                .index(index)
                .type(str)
                .content(orderItemReview.getContent())
                .createdAt(orderItemReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }

    default ProductDetailReviewDTO entityToProductDetailReviewDTO(OrderItemReview orderItemReview,long index){
        return ProductDetailReviewDTO.builder()
                .createdAt(orderItemReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-HH-dd")))
                .content(orderItemReview.getContent())
                .nickname(orderItemReview.getMember().getNickname())
                .index(index)
                .build();
    }


}

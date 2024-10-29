package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.OrderItem;
import hello.aiofirstuser.domain.OrderItemReview;
import hello.aiofirstuser.dto.order.OrderItemReviewRequestDTO;

public interface OrderItemReviewService {
    int save(OrderItemReviewRequestDTO orderItemReviewRequestDTO, String username);

    default OrderItemReview dtoToEntity(OrderItemReviewRequestDTO orderItemReviewRequestDTO, OrderItem orderItem){
       return OrderItemReview.builder()
                .grade(orderItemReviewRequestDTO.getGrade())
                .content(orderItemReviewRequestDTO.getContent())
                .member(orderItem.getOrder().getAddress().getMember())
                .orderItem(orderItem)
                .build();
    }


}

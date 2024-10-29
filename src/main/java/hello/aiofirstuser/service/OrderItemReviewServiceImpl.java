package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.OrderItem;
import hello.aiofirstuser.domain.OrderItemReview;
import hello.aiofirstuser.dto.order.OrderItemReviewRequestDTO;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.repository.OrderItemRepository;
import hello.aiofirstuser.repository.OrderItemReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemReviewServiceImpl implements OrderItemReviewService {

    private final MemberRepository memberRepository;
    private final OrderItemReviewRepository orderItemReviewRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public int save(OrderItemReviewRequestDTO orderItemReviewRequestDTO, String username) {
        Optional<OrderItem> result = orderItemRepository.getOrderItemByIdAndMemberUsername(username.toUpperCase(), orderItemReviewRequestDTO.getOrderItemId());

        OrderItem orderItem = result.orElse(null);
        boolean gradeCheck = orderItemReviewRequestDTO.getGrade() >=1 && orderItemReviewRequestDTO.getGrade() <=5;
        boolean contentCheck =!orderItemReviewRequestDTO.getContent().trim().isEmpty();
        if (orderItem == null || !gradeCheck || !contentCheck){
            return 0;
        }

        orderItemReviewRepository.save(dtoToEntity(orderItemReviewRequestDTO, orderItem));

        return 1;

    }
}

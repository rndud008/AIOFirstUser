package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.BaseEntity;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.OrderItem;
import hello.aiofirstuser.domain.OrderItemReview;
import hello.aiofirstuser.dto.custom.PostRequestDTO;
import hello.aiofirstuser.dto.custom.PostResponseDTO;
import hello.aiofirstuser.dto.order.OrderItemReviewDTO;
import hello.aiofirstuser.dto.order.OrderItemReviewRequestDTO;
import hello.aiofirstuser.repository.MemberRepository;
import hello.aiofirstuser.repository.OrderItemRepository;
import hello.aiofirstuser.repository.OrderItemReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemReviewServiceImpl implements OrderItemReviewService {

    private final MemberRepository memberRepository;
    private final OrderItemReviewRepository orderItemReviewRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public int save(OrderItemReviewRequestDTO orderItemReviewRequestDTO, String username) {
        Optional<OrderItem> result = orderItemRepository.getOrderItemByIdAndMemberUsername(username.toUpperCase(), orderItemReviewRequestDTO.getOrderItemId());

        OrderItem orderItem = result.orElse(null);
        boolean gradeCheck = orderItemReviewRequestDTO.getGrade() >= 1 && orderItemReviewRequestDTO.getGrade() <= 5;
        boolean contentCheck = !orderItemReviewRequestDTO.getContent().trim().isEmpty();
        if (orderItem == null || !gradeCheck || !contentCheck) {
            return 0;
        }

        orderItemReviewRepository.save(dtoToEntity(orderItemReviewRequestDTO, orderItem));

        return 1;

    }

    @Override
    @Transactional
    public int modify(PostRequestDTO postRequestDTO, Member member) {
        OrderItemReview orderItemReview = orderItemReviewRepository.findByMemberIdAndId(member.getId(), postRequestDTO.getId());

        if (orderItemReview == null){
            return 0;
        }

        orderItemReview.changeValue(postRequestDTO);

        return 1;
    }

    @Override
    public List<PostResponseDTO> getPostResponseDTOList(Member member) {
        List<OrderItemReview> orderItemReviews = orderItemReviewRepository.findByMemberId(member.getId());


        List<BaseEntity> baseEntities = new ArrayList<>();
        baseEntities.addAll(orderItemReviews);


        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        if (!baseEntities.isEmpty()) {
            baseEntities.sort(Comparator.comparing(BaseEntity::getCreatedAt).reversed());
            long index = baseEntities.size();
            for (BaseEntity base : baseEntities) {
                if (base instanceof OrderItemReview orderItemReview) {
                    postResponseDTOS.add(entityToPostResponseDTO(orderItemReview, "리뷰", index));
                }

                index--;
            }
        }

        return postResponseDTOS;
    }

    @Override
    public OrderItemReviewDTO getOrderItemReviewDTO(Member member, PostRequestDTO postRequestDTO) {
        OrderItemReview orderItemReview = orderItemReviewRepository.findByMemberIdAndId(member.getId(), postRequestDTO.getId());

        if (orderItemReview == null){
            return new OrderItemReviewDTO();
        }

        return entityToOrderItemReviewDTO(orderItemReview);
    }


}

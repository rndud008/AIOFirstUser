package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.*;
import hello.aiofirstuser.dto.member.MemberDTO;
import hello.aiofirstuser.dto.member.MyPageMemberDTO;
import hello.aiofirstuser.dto.member.OAuthResisterMemberDTO;
import hello.aiofirstuser.dto.member.OrderMemberDTO;
import hello.aiofirstuser.dto.order.OrderWriteDeliveryResponseDTO;
import hello.aiofirstuser.dto.order.OrderWriteDeliveryResponseListDTO;
import hello.aiofirstuser.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final OrderItemReviewRepository orderItemReviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final PointRepository pointRepository;
    private final AddressRepository addressRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MemberDTO save(OAuthResisterMemberDTO memberDTO) {

        List<Role> roles = Collections.singletonList(Role.MEMBER);

        Member member = Member.builder()
                .username(memberDTO.getUsername().toUpperCase())
                .nickname(memberDTO.getNickname())
                .provider(memberDTO.getProvider())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .roles(roles)
                .build();

        member = memberRepository.save(member);

        return entityToMemberDTO(member);
    }

    @Override
    public boolean exist(String username) {
        return memberRepository.existsByUsername(username.toUpperCase());
    }

    @Override
    public Member findByUsername(String username) {
        Member member = memberRepository.getWithRoles(username.toUpperCase());

        return member;
    }

    @Override
    public MemberDTO getWithRoles(String username) {
        Member member = memberRepository.getWithRoles(username.toUpperCase());

        if (member == null){
            return null;
        }

        return entityToMemberDTO(member);
    }

    @Override
    public OrderMemberDTO getOrderMember(String username) {
        Member member = memberRepository.findByUsername(username.toUpperCase());

        List<String> phoneNumber = new ArrayList<>();
        int length = String.valueOf(member.getPhoneNumber()).length();
        // 01001010202

        if (length >= 11) {
            String phoneNumberLast = String.valueOf(member.getPhoneNumber()).substring(length - 4, length);
            // 맨뒷자리 0202
            length -= 4;
            String phoneNumberMiddle = String.valueOf(member.getPhoneNumber()).substring(length - 4, length);
            // 중간자리 0101
            length -= 4;
            String phoneNumberFirst = String.valueOf(member.getPhoneNumber()).substring(0, length);
            //첫번째자리 010

            phoneNumber.add(phoneNumberLast);
            phoneNumber.add(phoneNumberMiddle);
            phoneNumber.add(phoneNumberFirst);
        }

        return entityToOrderMemberDTO(member, phoneNumber);
    }

    @Override
    public OrderWriteDeliveryResponseListDTO getOrderMemberAddresses(String username) {
        Member member = memberRepository.getWithAddresses(username.toUpperCase());

        List<Address> addresses = addressRepository.findByMemberId(member.getId());
        List<OrderWriteDeliveryResponseDTO> orderWriteDeliveryResponseDTOS = new ArrayList<>();

        if (!addresses.isEmpty()){
            for (Address address : addresses) {
                OrderWriteDeliveryResponseDTO orderWriteDeliveryResponseDTO = addressToOrderWriteDeliveryResponseDTO(address);
                orderWriteDeliveryResponseDTOS.add(orderWriteDeliveryResponseDTO);
            }
        }

        OrderWriteDeliveryResponseListDTO orderWriteDeliveryResponseListDTO = new OrderWriteDeliveryResponseListDTO();
        orderWriteDeliveryResponseListDTO.setOrderWriteDeliveryResponseDTOS(orderWriteDeliveryResponseDTOS);

        return orderWriteDeliveryResponseListDTO;
    }

    @Override
    public OrderWriteDeliveryResponseDTO getOrderMemberAddress(String username, String status) {
        Member member = memberRepository.getWithAddress(username.toUpperCase(), AddressStatus.valueOf(status));

        if (member != null) {
            Address address = member.getAddresses().get(0);

            return addressToOrderWriteDeliveryResponseDTO(address);
        }

        return new OrderWriteDeliveryResponseDTO();
    }

    @Override
    public OrderWriteDeliveryResponseDTO getOrderMemberAddressId(String username, Long addressId) {
       Member member = memberRepository.getWithAddressId(username.toUpperCase(),addressId);
       if(member != null){
           Address address = member.getAddresses().get(0);

           return addressToOrderWriteDeliveryResponseDTO(address);
       }

        return new OrderWriteDeliveryResponseDTO();
    }

    @Override
    public MyPageMemberDTO getMyPageMemberDTO(Member member) {
        int orderItemCount = orderItemRepository.orderItemCount(member.getId(), List.of(OrderStatus.ORDER_CANCELED));
        int writtenReviewCount  = orderItemReviewRepository.orderItemReviewCount(member.getId(), List.of(OrderStatus.ORDER_CANCELED));
        int unwrittenReviewCount = orderItemCount - writtenReviewCount;

        long currentPoint = 0L;
        List<Point> points = pointRepository.getStatusList(member.getId(), List.of(PointStatus.EARNED,PointStatus.USED));

        if(!points.isEmpty()){
            for(Point point: points){
                if (point.getPointStatus().equals(PointStatus.EARNED)){
                    currentPoint+= point.getPoint();
                } else if (point.getPointStatus().equals(PointStatus.USED)) {
                    currentPoint-= point.getPoint();
                }
            }
        }

        return entityToMyPageMemberDTO(member, unwrittenReviewCount, currentPoint);
    }


}

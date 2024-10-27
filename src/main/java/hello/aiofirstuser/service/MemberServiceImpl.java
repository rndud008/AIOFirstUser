package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Address;
import hello.aiofirstuser.domain.AddressStatus;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.Role;
import hello.aiofirstuser.dto.*;
import hello.aiofirstuser.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

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
        Member member = memberRepository.findByUsername(username.toUpperCase());

        return member;
    }

    @Override
    public MemberDTO getWithRoles(String username) {
        Member member = memberRepository.getWithRoles(username.toUpperCase());

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

        List<Address> addresses = member.getAddresses();
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


}

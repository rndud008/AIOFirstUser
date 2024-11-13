package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Address;
import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.member.MemberDTO;
import hello.aiofirstuser.dto.member.MyPageMemberDTO;
import hello.aiofirstuser.dto.member.OAuthResisterMemberDTO;
import hello.aiofirstuser.dto.member.OrderMemberDTO;
import hello.aiofirstuser.dto.order.OrderWriteDeliveryResponseDTO;
import hello.aiofirstuser.dto.order.OrderWriteDeliveryResponseListDTO;

import java.util.List;

public interface MemberService {

    MemberDTO save(OAuthResisterMemberDTO memberDTO);

    boolean exist(String username);

    Member findByUsername(String username);

    MemberDTO getWithRoles(String username);

    OrderMemberDTO getOrderMember(String username);

    OrderWriteDeliveryResponseListDTO getOrderMemberAddresses(String username);

    OrderWriteDeliveryResponseDTO getOrderMemberAddress(String username, String status);

    OrderWriteDeliveryResponseDTO getOrderMemberAddressId(String username, Long addressId);

    MyPageMemberDTO getMyPageMemberDTO(Member member);

    default MyPageMemberDTO entityToMyPageMemberDTO(Member member, int count, long currentPoint){
     return MyPageMemberDTO.builder()
             .username(member.getUsername())
             .nickname(member.getNickname())
             .writeReviewCount(count)
             .currentPoint(String.format("%,d",currentPoint)+"원")
             .build();
    }

    default MemberDTO entityToMemberDTO(Member member){

        MemberDTO memberDTO= MemberDTO.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .roles(member.getRoles().stream().map(role -> role.name()).toList())
                .build();

        return memberDTO;
    }

    default OrderMemberDTO entityToOrderMemberDTO(Member member, List<String> phoneNumber){
        return OrderMemberDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail() != null ? member.getEmail() : "" )
                .phoneNumber(phoneNumber.isEmpty() ? List.of("","","") : phoneNumber)
                .build();
    }

    default OrderWriteDeliveryResponseDTO addressToOrderWriteDeliveryResponseDTO(Address address){

        return OrderWriteDeliveryResponseDTO.builder()
                .addressId(address.getId())
                .deliveryMemberName(address.getNickname())
                .phoneNumber1(getPhoneNumber(address.getPhoneNumber1()))
                .phoneNumber2(getPhoneNumber(address.getPhoneNumber2()))
                .zipcode(address.getZipcode())
                .addressName(address.getAddressName())
                .addressNameDetail(address.getAddressNameDetail())
                .orderMessage(address.getOrderMessage())
                .bankTransferName(address.getBankTransferDepositor())
                .home(homeCheck(address.getAddressStatus().name()))
                .build();
    }

    private static boolean homeCheck(String name){

        if(name.equals("HOME_ADDRESS")){
            return true;
        }

        return false;
    }

    private static List<String> getPhoneNumber(long value){
        String number = String.valueOf(value);
        int numberLength = number.length();

        if(numberLength >= 9){
            String last = number.substring(numberLength-4,numberLength);

            numberLength -= 4;
            String middle = number.substring(numberLength-4,numberLength);

            numberLength -= 4;
            String first = number.substring(0,numberLength);

            return List.of(first,middle,last);
        }

        return List.of("","","");
    }

}

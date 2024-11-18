package hello.aiofirstuser.domain;

import hello.aiofirstuser.dto.order.OrderWriteRequestDTO;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private AddressStatus addressStatus;

    private String addressName;
    private String zipcode;
    private String addressNameDetail;

    private String nickname;
    private String orderMessage;
    private String bankTransferDepositor;// 무통장입금자

    private String phoneNumber1;
    private String phoneNumber2;

    public void changeAddress(OrderWriteRequestDTO orderWriteRequestDTO){

        this.addressStatus = AddressStatus.valueOf(orderWriteRequestDTO.getDeliveryPlaceStatus());
        this.addressName = orderWriteRequestDTO.getAddressName();
        this.addressNameDetail = orderWriteRequestDTO.getAddressNameDetail();
        this.zipcode = orderWriteRequestDTO.getZipcode();
        this.nickname = orderWriteRequestDTO.getDeliveryMemberName();
        this.orderMessage = orderWriteRequestDTO.getOrderMessage();
        this.bankTransferDepositor = orderWriteRequestDTO.getBankTransferName();
        this.phoneNumber1 = orderWriteRequestDTO.getPhoneNumber1();
        this.phoneNumber2 = orderWriteRequestDTO.getPhoneNumber2();

    }

    public void changeAddressStatus(AddressStatus addressStatus){
        this.addressStatus = addressStatus;
    }

}

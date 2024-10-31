package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.*;
import hello.aiofirstuser.dto.kakaopay.KakaoPayReadyRequestDTO;
import hello.aiofirstuser.dto.order.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface OrderService {

    OrderWriteResponseListDTO orderWriteResponseList(List<String> cartIdAndQuantity, Member member);

    KakaoPayReadyRequestDTO orderSave(String username, OrderWriteRequestDTO orderWriteRequestDTO);

    List<MyPageRecentlyOrderDTO> getOrderList(Member member,boolean top10);

    OrderDetailDTO getOrderDetailDTO(Long orderId,Member member);

    Order getOrderChangeStatus(Long orderId, String username);

    default MyPageRecentlyOrderDTO entityToMyPageRecentlyOrderDTO(Order order, int total, List<String> productNames,long index){

        return MyPageRecentlyOrderDTO.builder()
                .index(index)
                .orderId(order.getId())
                .orderDateTime(getFormatDateTime(order))
                .totalPrice(customString(total))
                .productNames(productNames)
                .productStatus(order.getOrderStauts().getDescription())
                .build();
    }

    default String getFormatDateTime(Order order) {
        return order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    default Order dtoToOrder(OrderWriteRequestDTO orderWriteRequestDTO, Address address){
        return Order.builder()
                .address(address)
                .paymentOption(orderWriteRequestDTO.getPaymentOption())
                .refundOption(orderWriteRequestDTO.getRefundOption())
                .refundMemberName(orderWriteRequestDTO.getRefundMemberName())
                .refundBankName(orderWriteRequestDTO.getRefundBankName())
                .refundBankAccount(orderWriteRequestDTO.getRefundBankAccount())
                .orderStauts(OrderStauts.PREPARING_ITEM)
                .build();
    }

    default Address dtoTOAddress(OrderWriteRequestDTO orderWriteRequestDTO, AddressStatus addressStatus, Member member) {

        return Address.builder()
                .addressName(orderWriteRequestDTO.getAddressName())
                .addressNameDetail(orderWriteRequestDTO.getAddressNameDetail())
                .zipcode(orderWriteRequestDTO.getZipcode())
                .addressStatus(addressStatus)
                .orderMessage(orderWriteRequestDTO.getOrderMessage())
                .bankTransferDepositor(orderWriteRequestDTO.getBankTransferName())
                .member(member)
                .nickname(orderWriteRequestDTO.getDeliveryMemberName())
                .phoneNumber1(orderWriteRequestDTO.getPhoneNumber1())
                .phoneNumber2(orderWriteRequestDTO.getPhoneNumber2())
                .build();
    }

    default OrderWriteResponseListDTO createOrderWriteResponseListDTO(List<OrderWriteResponseDTO> orderWriteResponseDTOS, int totalPrice, int totalPoint) {
        OrderWriteResponseListDTO orderWriteResponseListDTO = OrderWriteResponseListDTO.builder()
                .orderWriteResponseDTOS(orderWriteResponseDTOS)
                .totalPoint(customString(totalPoint))
                .totalPrice(customString(totalPrice))
                .lastTotalPrice(customString(deliveryCheck(totalPrice)))
                .totalPriceString(getTotalPriceString(totalPrice))
                .build();


        return orderWriteResponseListDTO;
    }

    default OrderWriteResponseDTO cartEntityToOrderWriteResponseDTO(Cart cart) {
        OrderWriteResponseDTO orderWriteResponseDTO = OrderWriteResponseDTO.builder()
                .productImg(cart.getProductVariant().getProduct().getProductImgs().get(0).getFileName())
                .productName(cart.getProductVariant().getProduct().getProductName())
                .productId(cart.getProductVariant().getProduct().getId())
                .cartId(cart.getId())
                .quantity(cart.getQuantity())
                .selectOption(customOrderWirteSelectOption(cart))
                .combineAndQuantityPrice(customPrice(cart))
                .point(customPoint(cart))
                .build();

        return orderWriteResponseDTO;
    }

    default String getTotalPriceString(int totalPrice) {

        String totalString = "";

        if (totalPrice >= 50000) {
            totalString = """
                                        
                        <span>총 상품 금액 </span>
                        <span>%,d</span>
                        <span> + </span>
                        <span>배송료</span>
                        <span>%,d</span>
                        <span> = </span>
                        <span>결제 예정금액 </span>
                        <span>%,d</span>
                                        
                    """.formatted(totalPrice, 0, totalPrice);
        } else {
            totalString = """
                                       
                        <span>총 상품 금액 </span>
                        <span>%,d</span>
                        <span> + </span>
                        <span>배송료</span>
                        <span>%,d</span>
                        <span> = </span>
                        <span>결제 예정금액 </span>
                        <span>%,d</span>
                                        
                    """.formatted(totalPrice, 3000, (totalPrice + 3000));
        }


        return totalString;
    }

    default int deliveryCheck(int value) {

        if (value >= 50000) {
            return value;
        } else {
            return value + 3000;
        }

    }

    default String customString(int value) {
        return String.format("%,d", value);
    }

    default String customPoint(Cart cart) {
        return String.format("%,d", combineAndQuantityPrice(cart) / 100);
    }

    default String customPrice(Cart cart) {
        return String.format("%,d", combineAndQuantityPrice(cart));
    }

    default int combineAndQuantityPrice(Cart cart) {
        return cart.getQuantity() * (cart.getProductVariant().getPrice() + cart.getProductVariant().getProduct().getSellPrice());
    }

    default String customOrderWirteSelectOption(Cart cart) {
        return "색상: " + cart.getProductVariant().getColor() + ", 사이즈: " + cart.getProductVariant().getSize() + " " + cart.getQuantity() + "개";
    }
}

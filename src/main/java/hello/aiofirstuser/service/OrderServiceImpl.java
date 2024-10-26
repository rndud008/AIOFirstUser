package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.*;
import hello.aiofirstuser.dto.*;
import hello.aiofirstuser.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderWriteResponseListDTO orderWriteResponseList(List<String> cartIdAndQuantity, Member member) {

        List<OrderWriteResponseDTO> orderWriteResponseDTOS = new ArrayList<>();

        int totalPrice = 0;
        int totalPoint = 0;

        for(String value : cartIdAndQuantity){

            Long cartId = Long.valueOf(value.split("-")[0]);
            int quantity = Integer.parseInt(value.split("-")[1]);

            Cart cart = cartRepository.findByCartIdAndMemberId(cartId, member.getId());
            if(cart == null){
                continue;
            }

            if(cart.getQuantity() != quantity){
                cart.changeQuantity(quantity);
            }

            OrderWriteResponseDTO orderWriteResponseDTO = cartEntityToOrderWriteResponseDTO(cart);
            orderWriteResponseDTOS.add(orderWriteResponseDTO);

            totalPrice += combineAndQuantityPrice(cart);
            totalPoint += combineAndQuantityPrice(cart)/100;
        }

        return createOrderWriteResponseListDTO(orderWriteResponseDTOS,totalPrice, totalPoint);
    }

    @Override
    public Long orderSave(String username, OrderWriteRequestDTO orderWriteRequestDTO) {
        Member member = memberRepository.findByUsername(username.toUpperCase());

        addressLogic(orderWriteRequestDTO, member);

        Order order = dtoToOrder(orderWriteRequestDTO, member);
        orderRepository.saveAndFlush(order);

        orderItemsLogic(orderWriteRequestDTO, member, order);

        return order.getId();
    }

    private void orderItemsLogic(OrderWriteRequestDTO orderWriteRequestDTO, Member member, Order order) {
        List<Cart> carts = cartRepository.findByMemberIdAndCartIds(orderWriteRequestDTO.getCartIds(), member.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : carts){
            OrderItem orderItem = OrderItem.builder()
                    .productVariant(cart.getProductVariant())
                    .order(order)
                    .quantity(cart.getQuantity())
                    .build();
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
//        cartRepository.deleteAllInBatch(carts);
    }

    private void addressLogic(OrderWriteRequestDTO orderWriteRequestDTO, Member member) {
        if(orderWriteRequestDTO.getAddressId().equals(0L)){
            Address address = addressRepository.findByMemberAddress(member.getId(), AddressStatus.RECENTLY_ADDRESS);

            if(address != null){
                address.changeAddressStatus(AddressStatus.OTHER_ADDRESS);
                addressRepository.save(address);
            }

            Address newAddress = dtoTOAddress(orderWriteRequestDTO,AddressStatus.RECENTLY_ADDRESS, member);

            addressRepository.save(newAddress);

        }else {
            Address address = addressRepository.findById(orderWriteRequestDTO.getAddressId()).orElse(null);

            if(address == null){
                address = dtoTOAddress(orderWriteRequestDTO,AddressStatus.valueOf(orderWriteRequestDTO.getDeliveryPlaceStatus()), member);
            }else {
                address.changeAddress(orderWriteRequestDTO);
            }
                addressRepository.save(address);
        }
    }
}

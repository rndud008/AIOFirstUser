package hello.aiofirstuser.service;

import hello.aiofirstuser.config.KakaoPayProperties;
import hello.aiofirstuser.domain.*;
import hello.aiofirstuser.dto.kakaopay.KakaoPayReadyRequestDTO;
import hello.aiofirstuser.dto.order.*;
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
    private final KakaoPayProperties kakaoPayProperties;
    private final PaymentRepository paymentRepository;
    private final OrderItemReviewRepository orderItemReviewRepository;
    private final PointRepository pointRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public OrderWriteResponseListDTO orderWriteResponseList(List<String> cartIdAndQuantity, Member member) {

        List<OrderWriteResponseDTO> orderWriteResponseDTOS = new ArrayList<>();

        int totalPrice = 0;
        int totalPoint = 0;

        for (String value : cartIdAndQuantity) {

            Long cartId = Long.valueOf(value.split("-")[0]);
            int quantity = Integer.parseInt(value.split("-")[1]);

            Cart cart = cartRepository.findByCartIdAndMemberId(cartId, member.getId());
            if (cart == null) {
                continue;
            }

            if (cart.getQuantity() != quantity) {
                cart.changeQuantity(quantity);
            }

            OrderWriteResponseDTO orderWriteResponseDTO = cartEntityToOrderWriteResponseDTO(cart);
            orderWriteResponseDTOS.add(orderWriteResponseDTO);

            totalPrice += combineAndQuantityPrice(cart);
            totalPoint += combineAndQuantityPrice(cart) / 100;
        }

        return createOrderWriteResponseListDTO(orderWriteResponseDTOS, totalPrice, totalPoint);
    }

    @Override
    public OrderWriteResponseListDTO orderWriteResponseList(List<OrderRequestDTO> orderRequestDTOS) {
        List<OrderWriteResponseDTO> orderWriteResponseDTOS = new ArrayList<>();

        int totalPrice = 0;
        int totalPoint = 0;

        for (OrderRequestDTO orderRequestDTO : orderRequestDTOS){

            ProductVariant productVariant = productVariantRepository
                    .findByProductIdAndColorAndSize(orderRequestDTO.getProductId(),orderRequestDTO.getColor(),orderRequestDTO.getSize());

            orderWriteResponseDTOS.add(productVariantEntityToOrderWriteResponseDTO(productVariant, orderRequestDTO.getQuantity()));

            totalPrice += (int) (orderRequestDTO.getQuantity() * (productVariant.getPrice() + productVariant.getProduct().getSellPrice()));
            totalPoint += (int) (orderRequestDTO.getQuantity() * (productVariant.getPrice() + productVariant.getProduct().getSellPrice()) /100);
        }

        return createOrderWriteResponseListDTO(orderWriteResponseDTOS, totalPrice, totalPoint);
    }

    @Override
    public KakaoPayReadyRequestDTO orderSave(String username, OrderWriteRequestDTO orderWriteRequestDTO) {
        Member member = memberRepository.findByUsername(username.toUpperCase());

        Address address = addressLogic(orderWriteRequestDTO, member);

        Order order = dtoToOrder(orderWriteRequestDTO, address);
        orderRepository.saveAndFlush(order);

        Result result = getResultAndOrderItemsSave(orderWriteRequestDTO, member, order);

        return getKakaoPayReadyRequestDTO(order, member, result.itemNames(), result.itemTotalQuantity(), result.itemTotalAmount());
    }

    private void pointSaveLogic(Member member, int itemTotalAmount, Order order) {
        Point point = pointRepository.findCurrentPointByMemberId(member.getId()).orElse(null);
        Point newPoint;
        if (point == null) {
            newPoint = Point.builder()
                    .member(member)
                    .order(order)
                    .pointStatus(PointStatus.PENDING)
                    .point(itemTotalAmount / 100)
                    .build();
        } else {
            newPoint = Point.builder()
                    .member(member)
                    .order(order)
                    .pointStatus(PointStatus.PENDING)
                    .point(itemTotalAmount / 100)
                    .build();
        }

        pointRepository.save(newPoint);
    }

    @Override
    public List<MyPageRecentlyOrderDTO> getOrderList(Member member, boolean top10) {

        List<PaymentStatus> paymentStatuses = List.of(PaymentStatus.SUCCESS, PaymentStatus.CANCEL);
        List<Order> orders;

        if (top10){
            orders = orderRepository.getRecentlyOrderListTop10(member.getId(), paymentStatuses);
        }else {
            orders = orderRepository.getOrderList(member.getId(), paymentStatuses);
        }

        List<MyPageRecentlyOrderDTO> myPageRecentlyOrderDTOS = new ArrayList<>();

        if(!orders.isEmpty()){
            long index = orders.size();
            for (Order order : orders) {
                List<String> productNames = new ArrayList<>();
                List<OrderItem> orderItems = orderItemRepository.getOrderItemList(order.getId());
                int total = 0;
                for (OrderItem orderItem : orderItems) {
                    productNames.add(selectOptionFullString(orderItem));
                    total += getOrderTotal(orderItem);
                }
                myPageRecentlyOrderDTOS.add(entityToMyPageRecentlyOrderDTO(order, total, productNames,index));
                index--;
            }
        }

        return myPageRecentlyOrderDTOS;
    }

    @Override
    public OrderDetailDTO getOrderDetailDTO(Long orderId, Member member) {
        Order order = orderRepository.getOrderIdAndMemberId(orderId, member.getId());
        if (order != null) {
            return getOrderDetailDTO(order);
        }
        return new OrderDetailDTO();
    }

    @Override
    @Transactional
    public Order getOrderChangeStatus(Long orderId, String username) {

        Order order = orderRepository.getOrderIdAndMemberUsername(orderId, username);

        if (order == null) {
            return null;
        }

        if (order.getOrderStatus().name().equals(OrderStatus.PREPARING_ITEM.name())) {
            order.changeStatus(OrderStatus.ORDER_CANCELED);

        }else {
            order.changeAdmin(true);

        }
            return order;

    }

    private OrderDetailDTO getOrderDetailDTO(Order order) {
        List<OrderItem> orderItems = orderItemRepository.getOrderItemList(order.getId());

        List<OrderDetailItemDTO> orderDetailItemDTOS = new ArrayList<>();

        boolean refundCheck = !order.getOrderStatus().equals(OrderStatus.ORDER_CANCELED) && !order.isAdminCheck();
        boolean adminCheck = !order.isAdminCheck();
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            orderDetailItemDTOS.add(getOrderDetailItemDTO(orderItem, order));
            totalPrice += getOrderTotal(orderItem);
        }


        return OrderDetailDTO.builder()
                .orderId(order.getId())
                .refundCheck(refundCheck)
                .adminCheck(adminCheck)
                .orderDateTime(getFormatDateTime(order))
                .orderStatus(order.getOrderStatus().getDescription())
                .deliveryNickname(order.getAddress().getNickname())
                .phoneNumber(orderDetailPhoneNumber(order))
                .fullAddressName("주소: " + "("+ order.getAddress().getZipcode() +") " + order.getAddress().getAddressName() + " " + order.getAddress().getAddressNameDetail())
                .lastTotalPrice(this.customString(totalPrice >= 50000 ? totalPrice : totalPrice + 3000))
                .totalPrice(this.customString(totalPrice))
                .deliveryPrice(this.customString(totalPrice >= 50000 ? 0 : 3000))
                .totalPoint(this.customString(totalPrice / 100))
                .orderDetailItemDTOS(orderDetailItemDTOS)
                .build();
    }

    private OrderDetailItemDTO getOrderDetailItemDTO(OrderItem orderItem, Order order) {
        boolean check = orderItemReviewRepository.orderItemReviewCheck(orderItem.getId()) == null
                && !order.getOrderStatus().name().equals(OrderStatus.ORDER_CANCELED.name())
                && !order.isAdminCheck();

        return OrderDetailItemDTO.builder()
                .productId(orderItem.getProductVariant().getProduct().getId())
                .orderItemId(orderItem.getId())
                .productName(orderItem.getProductVariant().getProduct().getProductName())
                .productImg(orderItem.getProductVariant().getProduct().getProductImgs().get(0).getFileName())
                .selectOption(selectOptionString(orderItem))
                .combineAndQuantity(this.customString(getOrderTotal(orderItem)))
                .reviewCheck(check)
                .build();
    }

    private List<String> orderDetailPhoneNumber(Order order) {
        List<String> resutNumbers = new ArrayList<>();

        String[] numbers = new String[]{String.valueOf(order.getAddress().getPhoneNumber1()), String.valueOf(order.getAddress().getPhoneNumber2())};

        for (String number : numbers) {
            int numberLength = number.length();

            if (numberLength >= 9) {
                String last = number.substring(numberLength - 4, numberLength);

                numberLength -= 4;
                String middle = number.substring(numberLength - 4, numberLength);

                numberLength -= 4;
                String first = number.substring(0, numberLength);

                resutNumbers.add(first + "-" + middle + "-" + last);
            }
        }

        return resutNumbers;

    }

    private static int getOrderTotal(OrderItem orderItem) {
        return orderItem.getQuantity() * (orderItem.getProductVariant().getPrice() + orderItem.getProductVariant().getProduct().getSellPrice());
    }

    private static String selectOptionFullString(OrderItem orderItem) {
        return orderItem.getProductVariant().getProduct().getProductName() +
                " 색상: " + orderItem.getProductVariant().getColor() +
                " 사이즈: " + orderItem.getProductVariant().getSize() +
                " " + orderItem.getQuantity() + "개";
    }

    private static String selectOptionString(OrderItem orderItem) {
        return "색상: " + orderItem.getProductVariant().getColor() +
                " 사이즈: " + orderItem.getProductVariant().getSize() +
                " " + orderItem.getQuantity() + "개";
    }

    private Result getResultAndOrderItemsSave(OrderWriteRequestDTO orderWriteRequestDTO, Member member, Order order) {

        List<OrderItem> orderItems = new ArrayList<>();
        String itemNames = "";
        int itemTotalQuantity = 0;
        int itemTotalAmount = 0;

        if (!orderWriteRequestDTO.getCartIds().isEmpty()){
            List<Cart> carts = cartRepository.findByMemberIdAndCartIds(orderWriteRequestDTO.getCartIds(), member.getId());
            for (Cart cart : carts) {
                OrderItem orderItem = OrderItem.builder()
                        .productVariant(cart.getProductVariant())
                        .order(order)
                        .quantity(cart.getQuantity())
                        .build();
                orderItems.add(orderItem);

                itemNames += cart.getProductVariant().getProduct().getProductName() + "(" + cart.getQuantity() + ") ";
                itemTotalQuantity += cart.getQuantity();
                itemTotalAmount += combineAndQuantityPrice(cart);
            }

        } else if (!orderWriteRequestDTO.getOrderProductVariantRequestDTOS().isEmpty()) {
            for (OrderProductVariantRequestDTO orderProductVariantRequestDTO : orderWriteRequestDTO.getOrderProductVariantRequestDTOS()){
                Optional<ProductVariant> result = productVariantRepository.findById(orderProductVariantRequestDTO.getProductVariantId());
                ProductVariant productVariant = result.orElse(new ProductVariant());

                if (productVariant.getId() == null){
                    continue;

                }

                OrderItem orderItem = OrderItem.builder()
                        .productVariant(productVariant)
                        .order(order)
                        .quantity(Math.toIntExact(orderProductVariantRequestDTO.getQuantity()))
                        .build();
                orderItems.add(orderItem);

                itemNames += productVariant.getProduct().getProductName() + "(" + orderProductVariantRequestDTO.getQuantity() + ") ";
                itemTotalQuantity += orderProductVariantRequestDTO.getQuantity();
                itemTotalAmount += (int) (orderProductVariantRequestDTO.getQuantity() * (productVariant.getPrice() + productVariant.getProduct().getSellPrice()));

            }

        }

        orderItemRepository.saveAll(orderItems);

        pointSaveLogic(member, itemTotalAmount, order);

        int last = itemNames.length() >= 100 ? 99 : itemNames.length();

        itemNames = itemNames.substring(0, last);
        itemTotalAmount = itemTotalAmount < 50000 ? itemTotalAmount + 3000 : itemTotalAmount;

        return new Result(itemNames, itemTotalQuantity, itemTotalAmount);

    }

    private record Result(String itemNames, int itemTotalQuantity, int itemTotalAmount) {
    }

    private KakaoPayReadyRequestDTO getKakaoPayReadyRequestDTO(Order order, Member member, String itemNames, int itemTotalQuantity, int itemTotalAmount) {

        return KakaoPayReadyRequestDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .partner_order_id(String.valueOf(order.getId()))
                .partner_user_id(String.valueOf(member.getId()))
                .tax_free_amount(0)
                .item_name(itemNames)
                .quantity(itemTotalQuantity)
                .total_amount(itemTotalAmount)
                .approval_url(kakaoPayProperties.getServer() + "/api/kakaopay/success")
                .cancel_url(kakaoPayProperties.getServer() + "/api/kakaopay/cancel")
                .fail_url(kakaoPayProperties.getServer() + "/api/kakaopay/fail")
                .build();
    }

    private Address addressLogic(OrderWriteRequestDTO orderWriteRequestDTO, Member member) {


        if (orderWriteRequestDTO.getAddressId() !=null && orderWriteRequestDTO.getAddressId().equals(0L)) {
            Address address = addressRepository.findByMemberAddress(member.getId(), AddressStatus.RECENTLY_ADDRESS);

            if (address != null) {
                address.changeAddressStatus(AddressStatus.OTHER_ADDRESS);
                addressRepository.save(address);
            }

            Address newAddress = dtoTOAddress(orderWriteRequestDTO, AddressStatus.RECENTLY_ADDRESS, member);

            addressRepository.save(newAddress);
            return newAddress;

        } else {
            Address address;
            if (orderWriteRequestDTO.getAddressId() != null){
                address = addressRepository.findById(orderWriteRequestDTO.getAddressId()).orElse(null);
                address.changeAddress(orderWriteRequestDTO);
            }else {
                address = dtoTOAddress(orderWriteRequestDTO, AddressStatus.valueOf(orderWriteRequestDTO.getDeliveryPlaceStatus()), member);
            }

            addressRepository.save(address);
            return address;
        }
    }
}

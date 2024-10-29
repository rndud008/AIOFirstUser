package hello.aiofirstuser.domain;

import lombok.Getter;

@Getter
public enum OrderStauts {
    PREPARING_ITEM("상품주문완료"), PREPARING_ITEM_CHECK("상품 준비중"), ADMIN_ITEM_CHECK("관리자 확인 필요")
    , DELIVERY_IN_PROGRESS("배송중"), DELIVERY_COMPLETED("배송완료"), ORDER_CANCELED("주문취소");

    private final String description;
    OrderStauts(String description){
        this.description = description;
    }



}

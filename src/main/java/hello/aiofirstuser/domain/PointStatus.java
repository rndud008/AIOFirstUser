package hello.aiofirstuser.domain;

import lombok.Getter;

@Getter
public enum PointStatus {

    PENDING("적립예정"), USED("사용"), EARNED("적립"), EARNED_CANCEL("적립취소");

    private final String description;

    PointStatus(String description){
        this.description = description;
    }
}

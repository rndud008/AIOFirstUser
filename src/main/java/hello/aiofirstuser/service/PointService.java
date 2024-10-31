package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.Point;
import hello.aiofirstuser.dto.point.MyPagePointDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface PointService {

    List<MyPagePointDTO> getMyPagePointDTOS(Member member);

    default MyPagePointDTO entityToMyPagePointDTO(Point point){
        return MyPagePointDTO.builder()
                .createdAt(point.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM")))
                .point(String.format("%,d",point.getPoint()))
                .currentPoint(String.format("%,d",point.getCurrentPoint()))
                .status(point.getPointStatus().getDescription())
                .build();
    }
}

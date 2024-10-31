package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.Point;
import hello.aiofirstuser.dto.point.MyPagePointDTO;
import hello.aiofirstuser.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public List<MyPagePointDTO> getMyPagePointDTOS(Member member) {
        List<Point> points = pointRepository.findByMemberId(member.getId());

        List<MyPagePointDTO> myPagePointDTOS = new ArrayList<>();

        if (points.isEmpty()){
            return myPagePointDTOS;
        }

        for (Point point : points){
            myPagePointDTOS.add(entityToMyPagePointDTO(point));
        }

        return myPagePointDTOS;
    }
}

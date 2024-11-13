package hello.aiofirstuser.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageMemberDTO {
    private String nickname;
    private String username;
    private String currentPoint;
    private int writeReviewCount;
}

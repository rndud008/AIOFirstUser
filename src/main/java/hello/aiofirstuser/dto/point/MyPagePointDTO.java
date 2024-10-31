package hello.aiofirstuser.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPagePointDTO {
    private String createdAt;
    private String point;
    private String status;
    private String currentPoint;
}

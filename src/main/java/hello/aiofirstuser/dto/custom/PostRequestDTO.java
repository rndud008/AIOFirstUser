package hello.aiofirstuser.dto.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequestDTO {
    private Long id;
    private String content;
    private Integer grade;
    private String type;

}

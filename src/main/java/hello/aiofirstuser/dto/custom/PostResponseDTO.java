package hello.aiofirstuser.dto.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDTO {
    private Long id;
    private Long index;
    private String type;
    private String content;
    private String createdAt;
}

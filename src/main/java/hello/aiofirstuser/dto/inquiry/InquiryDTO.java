package hello.aiofirstuser.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDTO {
    private Long inquiryId;
    private Long categoryId;
    private Long productId;
    private Long index;

    private String name;
    private String title;
    private String content;
    private String img;
    private String createdAt;

}

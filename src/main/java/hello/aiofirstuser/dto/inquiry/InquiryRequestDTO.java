package hello.aiofirstuser.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryRequestDTO {
    private Long inquiryId;
    private Long categoryId;
    private String name;
    private String password;
    private String title;
    private String content;

}

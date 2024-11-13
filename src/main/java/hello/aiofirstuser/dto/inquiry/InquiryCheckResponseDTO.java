package hello.aiofirstuser.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryCheckResponseDTO {
    private String categoryName;
    private Long inquiryId;
    private Long categoryId;
    private Long depno;
    private boolean answer;
}

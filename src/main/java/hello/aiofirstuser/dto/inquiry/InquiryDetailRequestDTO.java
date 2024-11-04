package hello.aiofirstuser.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDetailRequestDTO {
    private Long inquiryId;
    private String password;
}

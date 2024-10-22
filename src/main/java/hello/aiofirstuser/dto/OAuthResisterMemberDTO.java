package hello.aiofirstuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthResisterMemberDTO {
    private String username;
    private String nickname;
    private String password;
    private String provider;

}

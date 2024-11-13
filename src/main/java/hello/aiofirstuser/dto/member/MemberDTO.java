package hello.aiofirstuser.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {

    private String username;
    private String nickname;
    private List<String> roles;

    public Map<String, Object> getClaims(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("username",username);
        dataMap.put("nickname",nickname);
        dataMap.put("roles",roles);

        return dataMap;
    }

}

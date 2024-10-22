package hello.aiofirstuser.security.auth;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.MemberDTO;
import hello.aiofirstuser.service.MemberService;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails , OAuth2User {

    private MemberDTO memberDTO;
    private Map<String,Object> attributes;

    public PrincipalDetails(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    public PrincipalDetails(MemberDTO memberDTO, Map<String, Object> attributes) {
        this.memberDTO = memberDTO;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        if(memberDTO.getRoles() == null) return collection;

        memberDTO.getRoles().forEach(role -> collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.trim();
            }
            public String toString(){
                return role.trim();
            }
        }));

        return collection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}

package hello.aiofirstuser.security.auth;

import hello.aiofirstuser.dto.member.MemberDTO;
import hello.aiofirstuser.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDTO memberDTO = memberService.getWithRoles(username);

        if (memberDTO == null){
            throw new UsernameNotFoundException("NOT_FOUND");
        }

        return new PrincipalDetails(memberDTO);
    }
}

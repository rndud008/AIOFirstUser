package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.domain.Role;
import hello.aiofirstuser.dto.MemberDTO;
import hello.aiofirstuser.dto.OAuthResisterMemberDTO;
import hello.aiofirstuser.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MemberDTO save(OAuthResisterMemberDTO memberDTO) {

        List<Role> roles = Collections.singletonList(Role.MEMBER);

        Member member = Member.builder()
                .username(memberDTO.getUsername().toUpperCase())
                .nickname(memberDTO.getNickname())
                .provider(memberDTO.getProvider())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .roles(roles)
                .build();

        member = memberRepository.save(member);

        return entityToMemberDTO(member);
    }

    @Override
    public boolean exist(String username) {
        return memberRepository.existsByUsername(username.toUpperCase());
    }

    @Override
    public Member findByUsername(String username) {
        Member member = memberRepository.findByUsername(username.toUpperCase());

        return member;
    }

    @Override
    public MemberDTO getWithRoles(String username) {
        Member member = memberRepository.getWithRoles(username.toUpperCase());

        return entityToMemberDTO(member);
    }

}

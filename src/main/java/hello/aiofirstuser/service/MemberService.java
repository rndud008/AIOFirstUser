package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Member;
import hello.aiofirstuser.dto.MemberDTO;
import hello.aiofirstuser.dto.OAuthResisterMemberDTO;

public interface MemberService {

    MemberDTO save(OAuthResisterMemberDTO memberDTO);

    boolean exist(String username);

    Member findByUsername(String username);

    MemberDTO getWithRoles(String username);

    default MemberDTO entityToMemberDTO(Member member){

        MemberDTO memberDTO= MemberDTO.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .roles(member.getRoles().stream().map(role -> role.name()).toList())
                .build();

        return memberDTO;
    }
}

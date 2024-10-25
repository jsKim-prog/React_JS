package org.zerock.mallapi.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {
    //kakao 로그인 : accessToken -> memberDTO
    MemberDTO getKakaoMember(String accessToken);

    //kakao 로그인(미가입자) -> 회원정보 변경
    void modifyMember(MemberModifyDTO memberModifyDTO);

    default MemberDTO entityToDTO(Member member){
        MemberDTO dto = new MemberDTO(
                member.getEmail(), member.getPw(),
                member.getNickname(), member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
        return dto;
    }
}

package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;
import org.zerock.mallapi.repository.MemberRepository;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override //kakao 로그인 : accessToken -> memberDTO
    public MemberDTO getKakaoMember(String accessToken) {
        String email = getEmailFromKakaoAccessToken(accessToken);
        log.info("email : =====" +email);
        Optional<Member> result = memberRepository.findById(email);

        //기존회원
        if(result.isPresent()){
            MemberDTO memberDTO = entityToDTO(result.get());
            return memberDTO;
        }
        //미가입 회원
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);
        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;
    }

    @Override //kakao 로그인(미가입자) -> 회원정보 변경
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());
        Member member = result.orElseThrow();
        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeNickname(memberModifyDTO.getNickname());
        member.changeSocial(false);
        memberRepository.save(member);
    }

    //accessToken -> email
    private String getEmailFromKakaoAccessToken(String accessToken){
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me"; //사용자정보 받는 경로
        if(accessToken==null){
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);
        log.info("******"+ response);
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("******"+ bodyMap);
        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");
        log.info("kakaoAccount******"+ kakaoAccount);
        return kakaoAccount.get("email");
    }

    //임시비번 발급 : 미가입 회원->일반 로그인 불가(사용자, 관리자모두 모름)
    //카카오 로그인 후 회원정보 변경
    private String makeTempPassword(){ //임시비번 발급
        StringBuffer buffer = new StringBuffer();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            buffer.append((char) ((int)(Math.random()*55)+65));
        });
        return buffer.toString();
    }
    private Member makeSocialMember(String email){
        //미회원 가입자인 경우 새로운 엔티티 객체 생성
        String tempPassword = makeTempPassword();
        log.info("tempPassword : "+tempPassword);
        String nickname = "Social Member";
        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();
        member.addRole(MemberRole.USER);
        return member;
    }
}

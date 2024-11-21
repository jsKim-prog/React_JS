package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //멤버 등록
    @Test
    public void testInsertMember(){
        IntStream.rangeClosed(1, 5).forEach(i ->{
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("user"+i)
                    .build();
            member.addRole(MemberRole.USER);
            if(i>3){
                member.addRole(MemberRole.MANAGER);
            }
            memberRepository.save(member);
        });

    }

    //admin 등록
    @Test
    public void testInsertAd(){
        Member member = Member.builder()
                .email("admin@admin.com")
                .pw(passwordEncoder.encode("admin"))
                .nickname("관리자")
                .build();
        member.addRole(MemberRole.ADMIN);

        memberRepository.save(member);

    }

    //admin 권한 추가+조회
    @Test
    public void testRoleAdd(){
        String email = "admin@admin.com";
        Member member = memberRepository.getWithRoles(email);
        log.info("조회 멤버 : "+member);
        //조회 멤버 : Member(email=admin@admin.com, pw=$2a$10$AGEJLaAvcrHQMCX.zqENlOIWNTvs9aFsnTAPiZbK9ZiaGe/iUGHlm, nickname=관리자, social=false)
        member.clearRole();
        member.addRole(MemberRole.USER);
        member.addRole(MemberRole.MANAGER);
        member.addRole(MemberRole.ADMIN);

        memberRepository.save(member);
        log.info("멤버 권한 : "+member.getMemberRoleList()); //멤버 권한 : [USER, MANAGER, ADMIN]

    }



}

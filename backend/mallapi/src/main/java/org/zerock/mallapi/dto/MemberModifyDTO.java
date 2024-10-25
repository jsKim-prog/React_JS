package org.zerock.mallapi.dto;

import lombok.Data;

@Data
public class MemberModifyDTO { //security 적용
    private String email;
    private String pw;
    private String nickname;

}

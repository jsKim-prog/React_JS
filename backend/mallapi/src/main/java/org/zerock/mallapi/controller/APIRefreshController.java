package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.mallapi.util.CustomJWTException;
import org.zerock.mallapi.util.JWTUtil;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {
    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken){
        if(refreshToken==null){
            throw new CustomJWTException("NULL_REFRESH");
        }
        if(authHeader==null|| authHeader.length()<7){
            throw new CustomJWTException("INVALID_STRING");
        }
        String accessToken = authHeader.substring(7);
        //Access Token 만료 전
        if(checkExpiredToken(accessToken) == false){
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        //refresh token 검증
        Map<String, Object> claims = JWTUtil.validationToken(refreshToken);
        log.info("refreshToken: "+claims);
        String newAccessToken = JWTUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer) claims.get("exp"))==true?
                JWTUtil.generateToken(claims, 60*24):refreshToken; //1시간 미만 남아있으면 새로 발급

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    //토큰 만료 체크
    private boolean checkExpiredToken(String token){
        try {
            JWTUtil.validationToken(token);
        }catch (CustomJWTException ex){
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }

    //토큰 시간 계산(1시간 미만인지)
    private boolean checkTime(Integer exp){
        //JWT exp -> Date
        Date expDate = new Date((long) exp*1000);
        //현재시간과의 차이 (밀리세컨즈)
        long gap = expDate.getTime() - System.currentTimeMillis();
        //분단위 계산
        long leftMin = gap/(1000*60);
        return leftMin<60;
    }
}

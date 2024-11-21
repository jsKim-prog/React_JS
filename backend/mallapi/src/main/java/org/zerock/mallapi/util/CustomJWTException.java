package org.zerock.mallapi.util;

public class CustomJWTException extends RuntimeException{
    //JWT 관련 예외처리

    public CustomJWTException(String msg){
        super(msg);
    }
}

package org.zerock.mallapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable //값 타입 객체
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String fileName;
    private int ord; //이미지 순서(0:대표이미지)

    //메서드
    public void setOrd(int ord){
        this.ord = ord;
    }
}

package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag; //삭제여부-> 실제 삭제가 아닌, update 처리
    @ElementCollection //default : lazy loading
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void changePrice(int price){
        this.price = price;
    }

    public void changeName(String pname){
        this.pname = pname;
    }

    public void changeDesc(String pdesc){
        this.pdesc = pdesc;
    }

    public void addImage(ProductImage image){
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName){
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName).build();
        addImage(productImage);
    }

    public void clearList(){
        this.imageList.clear();
    }

    //상품삭제(p.205)
    public void changeDel(boolean delFlag){
        this.delFlag = delFlag;
    }

}

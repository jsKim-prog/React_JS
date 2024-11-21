package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.Todo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;


    //데이터 추가
    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,10).forEach(i->{
            Product product = Product.builder()
                    .pname("상품명"+i)
                    .price(100*i)
                    .pdesc("상품설명"+i).build();

            //2개의 이미지 파일 추가
            product.addImageString(UUID.randomUUID().toString()+"_"+"image1.jpg");
            product.addImageString(UUID.randomUUID().toString()+"_"+"image2.jpg");
            productRepository.save(product);
        });

    }

    //상품 확인
    @Test
    public void testRead(){
        Long pno = 1L;
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        log.info("가져온 상품 : "+product);
        log.info("가져온 상품리스트 : "+product.getImageList());
        //가져온 상품 : Product(pno=1, pname=상품명1, price=100, pdesc=상품설명1, delFlag=false, imageList=[ProductImage(fileName=c5e797de-75ef-403d-9896-02a6be8e644d_image1.jpg, ord=0), ProductImage(fileName=0ee5907f-2703-43a0-96c0-070d35d06a62_image2.jpg, ord=1)])
        //가져온 상품리스트 : [ProductImage(fileName=c5e797de-75ef-403d-9896-02a6be8e644d_image1.jpg, ord=0), ProductImage(fileName=0ee5907f-2703-43a0-96c0-070d35d06a62_image2.jpg, ord=1)] --select 1번
    }

    //상품 삭제
    @Commit //없으면 오류남
    @Transactional //없으면 오류남
    @Test
    public void testDelete(){
        Long pno = 2L;
        productRepository.updateToDelete(pno, true);
    }

    //상품리스트
    @Transactional
    @Test
    public void testList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
        //[Product(pno=6, pname=상품명6, price=600, pdesc=상품설명6, delFlag=false, imageList=[ProductImage(fileName=7af1b10f-e4d1-4378-8a66-03cbedc91c18_image1.jpg, ord=0), ProductImage(fileName=fe550a92-3667-421b-91f5-c32b06df53bb_image2.jpg, ord=1)]), ProductImage(fileName=7af1b10f-e4d1-4378-8a66-03cbedc91c18_image1.jpg, ord=0)]
    }

    //더미데이터 삭제
    @Test
    public void testDummyDel(){
        LongStream.rangeClosed(1L,10L).forEach(pno->{
            productRepository.deleteById(pno);
        });
    }

}

package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.dto.TodoDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {
    @Autowired
    ProductService productService;

    //등록 테스트
    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
                .pname("test pd")
                .pdesc("test product")
                .price(10)
                .build();
        productDTO.setUploadFileNames(List.of(UUID.randomUUID()+"_"+"test1.jpg", UUID.randomUUID()+"_"+"test2.jpg"));
        Long pno = productService.register(productDTO);
        log.info("Test 결과 등록된 상품 : " + pno); // Test 결과 등록된 상품 : 11
    }

    //리스트 테스트(페이징)
    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);
        result.getDtoList().forEach(dto->log.info(dto));
    }
}

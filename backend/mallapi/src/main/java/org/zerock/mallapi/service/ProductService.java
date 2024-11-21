package org.zerock.mallapi.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

@Transactional
public interface ProductService {
    //R: 상품목록
    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    //C:상품등록
    Long register(ProductDTO productDTO);

    //R:상품조회(p.223)
    ProductDTO get(Long pno);

    //U : 상품수정
    void modify(ProductDTO productDTO);

    //D: 상품삭제
    void remove(Long pno);


}

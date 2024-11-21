package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    //상품등록(+이미지파일)
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {
        log.info("(Controller)products register 실행====등록할 파일 : " + productDTO);
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        productDTO.setUploadFileNames(uploadFileNames);
        log.info(uploadFileNames);
        Long pno = productService.register(productDTO);
        return Map.of("result", pno);
    }

    //파일 불러오기
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }

    //상품리스트
    @PreAuthorize("hasRole('ROLE_ADMIN')") //임시 권한 설정
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("(Controller)products List 실행====page정보 : " + pageRequestDTO);
        return productService.getList(pageRequestDTO);
    }

    //상품조회
    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name = "pno") Long pno) {
        return productService.get(pno);
    }

    //상품수정
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDTO productDTO) {
        productDTO.setPno(pno);
        ProductDTO oldProductDTO = productService.get(pno);
        //기존 파일들
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        //새로 업로드 할 파일들
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //변화없이 유지된 파일들
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        //저장할 파일목록 : 유지된 파일+새로 업로드된 파일
        if (currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        //수정작업
        productService.modify(productDTO);

        //기존 파일 삭제
        if (oldFileNames != null && oldFileNames.size() > 0) {
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            fileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");
    }


    //상품삭제 처리(p.233)
    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno){
        //삭제할 파일(이미지) 조회
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);
        return Map.of("RESULT", "SUCCESS");
    }


}

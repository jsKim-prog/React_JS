package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.ProductImage;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("(Service)Product_List-----------");
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
        List<ProductDTO> dtoList = result.get().map(arr ->{
            //Object[Product(EN), ProductImage(EN)]
            //entity -> dto
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];
            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno()).pname(product.getPname())
                    .price(product.getPrice()).pdesc(product.getPdesc())
                    .build();
            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));
            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();
        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList).totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO).build();
    }

    @Override //C:상품등록
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);
        Product result = productRepository.save(product);

        return result.getPno();
    }

    @Override  //R:상품조회(p.223)
    public ProductDTO get(Long pno) {
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDTO productDTO = entityToDto(product);
        return productDTO;
    }

    @Override //U : 상품수정
    public void modify(ProductDTO productDTO) {
        //1. EN 가져오기
        Optional<Product> result = productRepository.findById(productDTO.getPno());
        Product product = result.orElseThrow();
        //2. dto 내용으로 변경
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(productDTO.getPrice());
        //3. imageList : clear -> 변경이미지 저장
        product.clearList();
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if(uploadFileNames != null|| uploadFileNames.size()>0){
            uploadFileNames.stream().forEach(uploadfileName ->{
                product.addImageString(uploadfileName);
            });
        }
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }

    private Product dtoToEntity(ProductDTO productDTO){
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();
        //업로드 완료된 파일이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if(uploadFileNames==null){
            return product;
        }
        uploadFileNames.stream().forEach(uploadName->
                product.addImageString(uploadName));
        return product;
    }

    private ProductDTO entityToDto(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .price(product.getPrice())
                .pdesc(product.getPdesc())
                .build();
        List<ProductImage> imageList = product.getImageList();
        if(imageList==null || imageList.size()==0){
            return productDTO;
        }
        List<String> fileNameList = imageList.stream().map(productImage ->
                productImage.getFileName()).toList();
        productDTO.setUploadFileNames(fileNameList);
        return productDTO;
    }
}

package org.zerock.mallapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //상품조회(with imageList)
    @EntityGraph(attributePaths = "imageList") //해당 속성 조인 처리->Transactional 없이 image list 출력
    @Query("select p from Product p where p.pno=:pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    //상품삭제(delFlag update)
    @Modifying
    @Query("update Product p set p.delFlag = :flag where p.pno=:pno")
    void updateToDelete(@Param("pno")Long pno, @Param("flag") boolean flag);

    //상품목록(1product+1 image)
    @Query("select p, pi from Product p left join p.imageList pi where pi.ord=0 and p.delFlag=false ")
    Page<Object[]> selectList(Pageable pageable);
}

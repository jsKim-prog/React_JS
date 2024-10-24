package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.TodoDTO;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {
    @Autowired
    private TodoService todoService;

    //등록 테스트
    @Test
    public void testRegister(){
        TodoDTO todoDTO = TodoDTO.builder()
                .title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2024, 10, 22))
                .build();
        Long tno = todoService.register(todoDTO);
        log.info("Test 결과 등록된 tno : " + tno); // Test 결과 등록된 tno : 101
    }

    //조회 테스트
    @Test
    public void testGet(){
        //tno -> dto
        Long tno = 101L;
        TodoDTO todoDTO = todoService.get(tno);
        log.info("Test 결과 조회된 todo : " + todoDTO);
        //Test 결과 조회된 todo : TodoDTO(tno=101, title=서비스 테스트, writer=tester, complete=false, dueDate=2024-10-22)
    }
}

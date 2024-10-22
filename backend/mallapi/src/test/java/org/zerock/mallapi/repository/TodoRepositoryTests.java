package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.domain.Todo;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;

    //객체 생성 테스트
    @Test
    public void test1(){
        log.info("Todo 객체생성 : ------" + todoRepository);
        //Todo 객체생성 : ------org.springframework.data.jpa.repository.support.SimpleJpaRepository@...
    }

    //데이터 추가
    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Todo todo = Todo.builder()
                    .title("todo 제목..."+i)
                    .dueDate(LocalDate.of(2024, 11, 28))
                    .writer("user00")
                    .build();
            todoRepository.save(todo);
        });
    }

}

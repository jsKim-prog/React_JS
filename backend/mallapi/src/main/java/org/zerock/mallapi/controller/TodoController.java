package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.service.TodoService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService service;

    //todo 1개 조회
    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable(name = "tno") Long tno){
        return service.get(tno);
    }
    //todo list 조회
    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        log.info("(Controller) 리스트 서비스/받은 페이지 정보 : "+ pageRequestDTO);
        return service.list(pageRequestDTO);
    }

    //todo 등록
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO){
        log.info("(Controller) 등록 서비스/받은 dto 정보 :"+todoDTO);
        Long tno = service.register(todoDTO);
        return Map.of("TNO", tno);
    }

    //todo 수정
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable(value = "tno") Long tno, @RequestBody TodoDTO todoDTO){
        log.info("(Controller) 수정 서비스---받은 tno/dto :"+tno+" / "+todoDTO);
        //todoDto setting : tno 삽입
        todoDTO.setTno(tno);
        service.modify(todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    //todo 삭제
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable(value = "tno") Long tno){
        log.info("(Controller) 삭제 서비스---받은 tno :"+tno);
        service.remove(tno);
        return Map.of("RESULT", "SUCCESS");
    }
}

package org.zerock.mallapi.service;

import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

public interface TodoService {
    //C: todo 등록(dto -> entity)
    Long register(TodoDTO todoDTO);
    
    //R : todo 조회(tno -> dto)
    TodoDTO get(Long tno);

    //U : todo 변경
    void modify(TodoDTO todoDTO);

    //D : todo 삭제
    void remove(Long tno);

    //R : todo list 조회(Paging)
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
}

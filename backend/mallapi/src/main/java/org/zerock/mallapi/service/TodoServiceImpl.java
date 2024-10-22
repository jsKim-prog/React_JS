package org.zerock.mallapi.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor //생성자 자동 주입
public class TodoServiceImpl implements TodoService{
    //자동주입 대상은 final 로
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;

    @Override  //C: todo 등록
    public Long register(TodoDTO todoDTO) {
        log.info("(Service)Todo 등록 실행/받은 dto : "+todoDTO);
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        Todo savedToto = todoRepository.save(todo);
        return savedToto.getTno();
    }

    @Override //R : todo 조회
    public TodoDTO get(Long tno) {
        //**findBy 결과는 Optional<>
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        TodoDTO dto = modelMapper.map(todo, TodoDTO.class);

        return dto;
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        //entity 조회 -> dto의 내용으로 entity 변경 -> entity save
        Todo findTodo = todoRepository.findById(todoDTO.getTno()).orElseThrow(EntityExistsException::new);
        findTodo.changeTitle(todoDTO.getTitle());
        findTodo.changeComplete(todoDTO.isComplete());
        findTodo.changeDueDate(todoDTO.getDueDate());

        todoRepository.save(findTodo);
    }

    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        //page 조건
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("tno").descending());
        //PageResponseDTO 조건(dtoList, pageRequestDTO, totalCount)
        Page<Todo> result = todoRepository.findAll(pageable); //entity list
        List<TodoDTO> dtoList = result.getContent().stream().map(todo -> modelMapper.map(todo, TodoDTO.class)).collect(Collectors.toList()); //entity list -> dto list
        long totalCount = result.getTotalElements();

        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll().dtoList(dtoList).pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();
        return responseDTO;
    }
}

package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.category.CategoryDTO;
import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import com.jiyunio.todolist.responseDTO.ResponseCategoryDTO;
import com.jiyunio.todolist.responseDTO.ResponseTodoDTO;
import com.jiyunio.todolist.todo.dto.CreateTodoDTO;
import com.jiyunio.todolist.todo.dto.UpdateTodoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    public ResponseTodoDTO createTodo(Long memberId, CreateTodoDTO createTodo) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                // 회원 존재 안함
                () -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_EXIST_MEMBER)
        );

        Todo todo = Todo.builder()
                .member(member)
                .content(createTodo.getContent())
                .setDate(createTodo.getSetDate())
                .checked(false)
                .categoryId(createTodo.getCategory().getCategoryId())
                .categoryContent(createTodo.getCategory().getContent())
                .categoryColor(createTodo.getCategory().getColor())
                .build();

        todoRepository.save(todo);

        return ResponseTodoDTO.builder()
                .todoId(todo.getId())
                .content(todo.getContent())
                .checked(todo.getChecked())
                .setDate(todo.getSetDate())
                .category(ResponseCategoryDTO.builder()
                        .categoryId(todo.getCategoryId())
                        .content(todo.getContent())
                        .color(todo.getCategoryColor())
                        .build())
                .build();
    }

    public List<ResponseTodoDTO> getTodo(Long memberId) {
        List<Todo> todoList = todoRepository.findByMemberId(memberId);
        if (todoList == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_EXIST_MEMBER);
        }

        List<ResponseTodoDTO> getTodoList = new ArrayList<>();

        for (Todo todo : todoList) {
            getTodoList.add(ResponseTodoDTO.builder()
                    .todoId(todo.getId())
                    .content(todo.getContent())
                    .setDate(todo.getSetDate())
                    .checked(todo.getChecked())
                    .category(ResponseCategoryDTO.builder()
                            .categoryId(todo.getCategoryId())
                            .content(todo.getCategoryContent())
                            .color(todo.getCategoryColor())
                            .build())
                    .build());
        }
        return getTodoList;
    }

    public ResponseTodoDTO updateTodo(Long todoId, UpdateTodoDTO updateTodo) {
        Todo todo = todoRepository.findById(todoId).get();
        todo.updateTodo(updateTodo);
        todoRepository.save(todo);

        return ResponseTodoDTO.builder()
                .todoId(todo.getId())
                .content(todo.getContent())
                .checked(todo.getChecked())
                .setDate(todo.getSetDate())
                .category(ResponseCategoryDTO.builder()
                        .categoryId(todo.getCategoryId())
                        .content(todo.getCategoryContent())
                        .color(todo.getCategoryColor())
                        .build())
                .build();
    }

    public void updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        List<Todo> todoList = todoRepository.findByCategoryId(categoryId);
        for (Todo todo : todoList) {
            todo.updateCategory(categoryDTO);
            todoRepository.save(todo);
        }
    }

    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}

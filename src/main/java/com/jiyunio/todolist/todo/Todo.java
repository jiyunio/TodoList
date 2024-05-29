package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.category.Category;
import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.todo.dto.GetUpdateTodoDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todoId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Lob // 길이 제한 X
    private String content;

    private Boolean checked;

    private String category;

    private LocalDate writeDate;

    private LocalDate setDate;

    private String color;

    @Builder
    protected Todo(Member member, String content, Boolean checked, String category,
                   LocalDate writeDate, LocalDate setDate, String color) {
        this.member = member;
        this.content = content;
        this.checked = checked;
        this.category = category;
        this.writeDate = writeDate;
        this.setDate = setDate;
        this.color = color;
    }

    protected void updateTodo(GetUpdateTodoDTO getUpdateTodoDto) {
        this.content = getUpdateTodoDto.getContent();
        this.checked = getUpdateTodoDto.getChecked();
        this.writeDate = getUpdateTodoDto.getWriteDate();
        this.setDate = getUpdateTodoDto.getSetDate();
        this.category = getUpdateTodoDto.getCategory();
    }
}
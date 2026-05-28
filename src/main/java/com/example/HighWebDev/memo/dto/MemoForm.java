package com.example.HighWebDev.memo.dto;

import com.example.HighWebDev.memo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class MemoForm {
    private Long id;
    private String title;
    private String content;

    public Memo toEntity(String username) {
        return new Memo(id, username, title, content, null);
    }
}

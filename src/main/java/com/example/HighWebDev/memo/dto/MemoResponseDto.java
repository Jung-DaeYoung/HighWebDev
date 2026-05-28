package com.example.HighWebDev.memo.dto;

import com.example.HighWebDev.memo.entity.Memo;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public class MemoResponseDto {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM/dd HH:mm");

    private final Long id;
    private final String title;
    private final String content;
    private final String formattedTime;

    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.title = memo.getTitle() != null ? memo.getTitle() : "";
        this.content = memo.getContent() != null ? memo.getContent() : "";
        this.formattedTime = memo.getCreatedAt().format(FORMAT);
    }
}

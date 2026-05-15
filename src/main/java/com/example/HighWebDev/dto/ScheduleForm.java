package com.example.HighWebDev.dto;

import com.example.HighWebDev.entity.Schedule;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class ScheduleForm {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime deadline;

    public Schedule toEntity() {
        return new Schedule(id, title, content, deadline, null);
    }
}

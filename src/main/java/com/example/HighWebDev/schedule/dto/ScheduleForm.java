package com.example.HighWebDev.schedule.dto;

import com.example.HighWebDev.schedule.entity.Schedule;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class ScheduleForm {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime deadline;

    public Schedule toEntity(String username) {
        return new Schedule(id, username, title, content, deadline, null);
    }
}

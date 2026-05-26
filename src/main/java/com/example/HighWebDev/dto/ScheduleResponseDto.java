package com.example.HighWebDev.dto;

import com.example.HighWebDev.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String dDay;
    private final String urgencyClass;
    private final String formattedDeadline;
    private final String displayDeadline;
    private final String color;

    public ScheduleResponseDto(Schedule schedule, String dDay, String urgencyClass,
                               String formattedDeadline, String displayDeadline, String color) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.dDay = dDay;
        this.urgencyClass = urgencyClass;
        this.formattedDeadline = formattedDeadline;
        this.displayDeadline = displayDeadline;
        this.color = color;
    }
}
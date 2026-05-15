package com.example.HighWebDev.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private LocalDateTime deadline;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public String getDDay() {
        LocalDateTime now = LocalDateTime.now();
        if (deadline == null) return "미정";

        if (deadline.isBefore(now)) {
            return "마감됨";
        }

        java.time.LocalDate today = now.toLocalDate();
        java.time.LocalDate deadlineDate = deadline.toLocalDate();
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, deadlineDate);

        if (daysLeft > 0) {
            return "D-" + daysLeft;
        } else {
            // 오늘 마감인 경우 시간 단위로 표시
            java.time.Duration duration = java.time.Duration.between(now, deadline);
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            if (hours > 0) {
                return hours + "시간 전";
            } else {
                return minutes + "분 전";
            }
        }
    }

    public String getUrgencyClass() {
        if (deadline == null) return "bg-secondary";
        java.time.Duration duration = java.time.Duration.between(LocalDateTime.now(), deadline);
        
        if (duration.isNegative()) return "bg-danger"; // 마감됨
        if (duration.toHours() <= 24) return "bg-warning text-dark"; // 24시간 이내
        return "bg-success"; // 여유 있음
    }

    // HTML input (datetime-local) 포맷에 맞게 반환 (초 제외)
    public String getFormattedDeadline() {
        if (deadline == null) return "";
        return deadline.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    // 화면에 예쁘게 표시하기 위한 포맷
    public String getDisplayDeadline() {
        if (deadline == null) return "미정";
        return deadline.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void patch(Schedule schedule) {
        if (schedule.title != null)
            this.title = schedule.title;
        if (schedule.content != null)
            this.content = schedule.content;
        if (schedule.deadline != null)
            this.deadline = schedule.deadline;
    }
}

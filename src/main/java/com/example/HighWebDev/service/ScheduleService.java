package com.example.HighWebDev.service;

import com.example.HighWebDev.dto.ScheduleResponseDto;
import com.example.HighWebDev.entity.Schedule;
import com.example.HighWebDev.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> findAllByUsername(String username) {
        return scheduleRepository.findAllByUsernameOrderByIdAsc(username);
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    public ScheduleResponseDto toResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule,
                getDDay(schedule.getDeadline()),
                getUrgencyClass(schedule.getDeadline()),
                getFormattedDeadline(schedule.getDeadline()),
                getDisplayDeadline(schedule.getDeadline())
        );
    }

    public String getDDay(LocalDateTime deadline) {
        if (deadline == null) return "미정";
        LocalDateTime now = LocalDateTime.now();
        if (deadline.isBefore(now)) return "마감됨";
        long daysLeft = ChronoUnit.DAYS.between(now.toLocalDate(), deadline.toLocalDate());
        if (daysLeft > 0) return "D-" + daysLeft;
        Duration duration = Duration.between(now, deadline);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        if (hours > 0) return hours + "시간 전";
        return minutes + "분 전";
    }

    public String getUrgencyClass(LocalDateTime deadline) {
        if (deadline == null) return "bg-secondary";
        Duration duration = Duration.between(LocalDateTime.now(), deadline);
        if (duration.isNegative()) return "bg-danger";
        if (duration.toHours() <= 24) return "bg-warning text-dark";
        return "bg-success";
    }

    public String getFormattedDeadline(LocalDateTime deadline) {
        if (deadline == null) return "";
        return deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public String getDisplayDeadline(LocalDateTime deadline) {
        if (deadline == null) return "미정";
        return deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
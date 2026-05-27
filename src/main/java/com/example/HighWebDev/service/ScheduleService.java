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
    private static final String[] RAINBOW = {"#e53935", "#fb8c00", "#fdd835", "#43a047", "#1e88e5", "#3949ab", "#8e24aa"};

    public List<ScheduleResponseDto> findAllByUsername(String username, String sort) {
        List<Schedule> schedules = scheduleRepository.findAllByUsernameOrderByIdAsc(username);
        LocalDateTime now = LocalDateTime.now();

        if ("deadline".equals(sort)) {
            // 마감 임박순 정렬 로직: [미래 일정(가까운순) -> 과거 일정(최근순)]
            List<Schedule> future = schedules.stream()
                    .filter(s -> s.getDeadline() != null && s.getDeadline().isAfter(now))
                    .sorted((a, b) -> a.getDeadline().compareTo(b.getDeadline()))
                    .toList();

            List<Schedule> past = schedules.stream()
                    .filter(s -> s.getDeadline() == null || !s.getDeadline().isAfter(now))
                    .sorted((a, b) -> {
                        if (a.getDeadline() == null && b.getDeadline() == null) return 0;
                        if (a.getDeadline() == null) return 1;
                        if (b.getDeadline() == null) return -1;
                        return b.getDeadline().compareTo(a.getDeadline());
                    })
                    .toList();

            schedules = new java.util.ArrayList<>();
            schedules.addAll(future);
            schedules.addAll(past);
        }

        // DTO 변환 및 색상 부여
        List<ScheduleResponseDto> dtos = new java.util.ArrayList<>();
        for (int i = 0; i < schedules.size(); i++) {
            String color = RAINBOW[i % RAINBOW.length];
            dtos.add(toResponseDto(schedules.get(i), color));
        }
        return dtos;
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

    public ScheduleResponseDto toResponseDto(Schedule schedule, String color) {
        return new ScheduleResponseDto(
                schedule,
                getDDay(schedule.getDeadline()),
                getUrgencyClass(schedule.getDeadline()),
                getFormattedDeadline(schedule.getDeadline()),
                getDisplayDeadline(schedule.getDeadline()),
                color
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

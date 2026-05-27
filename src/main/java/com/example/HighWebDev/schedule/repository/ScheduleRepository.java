package com.example.HighWebDev.schedule.repository;

import com.example.HighWebDev.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByUsernameOrderByIdAsc(String username);

    List<Schedule> findAllByUsernameOrderByDeadlineAsc(String username);

    List<Schedule> findAllByOrderByDeadlineAsc();
}

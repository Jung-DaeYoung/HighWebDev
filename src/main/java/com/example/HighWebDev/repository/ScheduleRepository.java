package com.example.HighWebDev.repository;

import com.example.HighWebDev.entity.Schedule;
import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    @Override
    ArrayList<Schedule> findAll();

    // 마감 기한 임박순으로 정렬하여 조회
    ArrayList<Schedule> findAllByOrderByDeadlineAsc();
}

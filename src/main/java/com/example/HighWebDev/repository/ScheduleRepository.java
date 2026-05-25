package com.example.HighWebDev.repository;

import com.example.HighWebDev.entity.Schedule;
import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

    ArrayList<Schedule> findAllByUsernameOrderByIdAsc(String username);

    ArrayList<Schedule> findAllByOrderByIdAsc();
}
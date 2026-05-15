package com.example.HighWebDev.repository;

import com.example.HighWebDev.entity.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("마감 기한 임박순 정렬 테스트")
    void findAllByOrderByDeadlineAscTest() {
        // 1. 준비 (초기 데이터는 data.sql에 의해 삽입됨)
        // data.sql의 가장 빠른 일정은 '운영체제 퀴즈' (2026-05-18)

        // 2. 실행
        List<Schedule> schedules = scheduleRepository.findAllByOrderByDeadlineAsc();

        // 3. 검증
        assertEquals("운영체제 퀴즈", schedules.get(0).getTitle(), "첫 번째 일정은 운영체제 퀴즈여야 함");
        assertEquals("팀 프로젝트 기획안 제출", schedules.get(1).getTitle(), "두 번째 일정은 팀 프로젝트 기획안 제출이어야 함");
        
        // 정렬 확인 (이전 마감시간 <= 다음 마감시간)
        for (int i = 0; i < schedules.size() - 1; i++) {
            LocalDateTime current = schedules.get(i).getDeadline();
            LocalDateTime next = schedules.get(i + 1).getDeadline();
            assert(current.isBefore(next) || current.isEqual(next));
        }
    }
}

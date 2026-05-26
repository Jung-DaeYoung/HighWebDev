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
        // 1. 준비
        Schedule s1 = new Schedule(null, "user1", "늦은 일정", "내용", LocalDateTime.of(2026, 12, 31, 23, 59), null);
        Schedule s2 = new Schedule(null, "user1", "빠른 일정", "내용", LocalDateTime.of(2026, 1, 1, 0, 0), null);
        scheduleRepository.save(s1);
        scheduleRepository.save(s2);

        // 2. 실행
        List<Schedule> schedules = scheduleRepository.findAllByOrderByDeadlineAsc();

        // 3. 검증
        assertEquals("빠른 일정", schedules.get(0).getTitle(), "가장 빠른 일정이 첫 번째여야 함");
        
        // 정렬 확인 (이전 마감시간 <= 다음 마감시간)
        for (int i = 0; i < schedules.size() - 1; i++) {
            LocalDateTime current = schedules.get(i).getDeadline();
            LocalDateTime next = schedules.get(i + 1).getDeadline();
            assert(current.isBefore(next) || current.isEqual(next));
        }
    }
}

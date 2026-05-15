package com.example.HighWebDev.controller;

import com.example.HighWebDev.dto.ScheduleForm;
import com.example.HighWebDev.entity.Schedule;
import com.example.HighWebDev.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/schedules")
    public String index(Model model) {
        // 1. 모든 일정 가져오기 (마감 기한 임박순)
        List<Schedule> scheduleEntityList = scheduleRepository.findAllByOrderByDeadlineAsc();

        // 2. 모델에 데이터 등록
        model.addAttribute("scheduleList", scheduleEntityList);

        // 3. 뷰 페이지 설정
        return "schedules/index";
    }

    @GetMapping("/schedules/new")
    public String newScheduleForm() {
        return "schedules/new";
    }

    @PostMapping("/schedules/create")
    public String createSchedule(ScheduleForm form) {
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환
        Schedule schedule = form.toEntity();

        // 2. 리포지토리로 엔티티를 DB에 저장
        Schedule saved = scheduleRepository.save(schedule);
        log.info(saved.toString());

        return "redirect:/schedules/" + saved.getId();
    }

    @GetMapping("/schedules/{id}")
    public String show(@PathVariable Long id, Model model) {
        // 1. id를 조회해 데이터 가져오기
        Schedule scheduleEntity = scheduleRepository.findById(id).orElse(null);

        // 2. 모델에 데이터 등록
        model.addAttribute("schedule", scheduleEntity);

        // 3. 뷰 페이지 반환
        return "schedules/show";
    }

    @GetMapping("/schedules/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // 1. 수정할 데이터 가져오기
        Schedule scheduleEntity = scheduleRepository.findById(id).orElse(null);

        // 2. 모델에 데이터 등록
        model.addAttribute("schedule", scheduleEntity);

        // 3. 뷰 페이지 설정
        return "schedules/edit";
    }

    @PostMapping("/schedules/update")
    public String update(ScheduleForm form) {
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환
        Schedule scheduleEntity = form.toEntity();

        // 2. id로 기존 엔티티를 조회
        Schedule target = scheduleRepository.findById(scheduleEntity.getId()).orElse(null);

        // 3. 기존 데이터 갱신
        if (target != null) {
            target.patch(scheduleEntity);
            scheduleRepository.save(target);
        }

        return "redirect:/schedules/" + scheduleEntity.getId();
    }

    @GetMapping("/schedules/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다!!");

        // 1. 삭제 대상 가져오기
        Schedule target = scheduleRepository.findById(id).orElse(null);

        // 2. 대상 삭제하기
        if (target != null) {
            scheduleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제되었습니다!");
        }

        // 3. 결과 페이지로 리다이렉트
        return "redirect:/schedules";
    }
}

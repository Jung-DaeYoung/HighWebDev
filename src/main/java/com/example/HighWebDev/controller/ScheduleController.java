package com.example.HighWebDev.controller;

import com.example.HighWebDev.dto.ScheduleForm;
import com.example.HighWebDev.entity.Schedule;
import com.example.HighWebDev.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            List<Schedule> scheduleEntityList = scheduleRepository.findAllByUsernameOrderByIdAsc(userDetails.getUsername());
            model.addAttribute("scheduleList", scheduleEntityList);
        } else {
            model.addAttribute("scheduleList", List.of());
        }
        return "schedules/index";
    }

    @GetMapping("/schedules")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Schedule> scheduleEntityList = scheduleRepository.findAllByUsernameOrderByIdAsc(userDetails.getUsername());
        model.addAttribute("scheduleList", scheduleEntityList);
        return "schedules/index";
    }

    @GetMapping("/schedules/new")
    public String newScheduleForm() {
        return "schedules/new";
    }

    @PostMapping("/schedules/create")
    public String createSchedule(ScheduleForm form,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Schedule schedule = form.toEntity(userDetails.getUsername());
        Schedule saved = scheduleRepository.save(schedule);
        return "redirect:/schedules/" + saved.getId();
    }

    @GetMapping("/schedules/{id}")
    public String show(@PathVariable Long id, Model model) {
        Schedule scheduleEntity = scheduleRepository.findById(id).orElse(null);
        model.addAttribute("schedule", scheduleEntity);
        return "schedules/show";
    }

    @GetMapping("/schedules/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Schedule scheduleEntity = scheduleRepository.findById(id).orElse(null);
        model.addAttribute("schedule", scheduleEntity);
        return "schedules/edit";
    }

    @PostMapping("/schedules/update")
    public String update(ScheduleForm form,
                         @AuthenticationPrincipal UserDetails userDetails) {
        Schedule scheduleEntity = form.toEntity(userDetails.getUsername());
        Schedule target = scheduleRepository.findById(scheduleEntity.getId()).orElse(null);
        if (target != null) {
            target.patch(scheduleEntity);
            scheduleRepository.save(target);
        }
        return "redirect:/schedules/" + scheduleEntity.getId();
    }

    @GetMapping("/schedules/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Schedule target = scheduleRepository.findById(id).orElse(null);
        if (target != null) {
            scheduleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제되었습니다!");
        }
        return "redirect:/";
    }
}
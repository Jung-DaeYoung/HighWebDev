package com.example.HighWebDev.controller;

import com.example.HighWebDev.dto.ScheduleForm;
import com.example.HighWebDev.dto.ScheduleResponseDto;
import com.example.HighWebDev.entity.Schedule;
import com.example.HighWebDev.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("scheduleList", List.of());
            return "schedules/index";
        }
        List<ScheduleResponseDto> scheduleList = scheduleService.findAllByUsername(userDetails.getUsername())
                .stream()
                .map(scheduleService::toResponseDto)
                .toList();
        model.addAttribute("scheduleList", scheduleList);
        return "schedules/index";
    }

    @GetMapping("/new")
    public String newScheduleForm() {
        return "schedules/new";
    }

    @PostMapping("/create")
    public String createSchedule(ScheduleForm form,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Schedule schedule = form.toEntity(userDetails.getUsername());
        Schedule saved = scheduleService.save(schedule);
        return "redirect:/schedules/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleService.findById(id);
        if (schedule != null) {
            model.addAttribute("schedule", scheduleService.toResponseDto(schedule));
        }
        return "schedules/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleService.findById(id);
        if (schedule != null) {
            model.addAttribute("schedule", scheduleService.toResponseDto(schedule));
        }
        return "schedules/edit";
    }

    @PostMapping("/update")
    public String update(ScheduleForm form,
                         @AuthenticationPrincipal UserDetails userDetails) {
        Schedule scheduleEntity = form.toEntity(userDetails.getUsername());
        Schedule target = scheduleService.findById(scheduleEntity.getId());
        if (target != null) {
            target.patch(scheduleEntity);
            scheduleService.save(target);
        }
        return "redirect:/schedules/" + scheduleEntity.getId();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return "ok";
    }
}
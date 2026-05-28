package com.example.HighWebDev.memo.controller;

import com.example.HighWebDev.memo.dto.MemoForm;
import com.example.HighWebDev.memo.entity.Memo;
import com.example.HighWebDev.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/create")
    public String create(MemoForm form, Principal principal) {
        Memo memo = form.toEntity(principal.getName());
        memoService.save(memo);
        return "redirect:/schedules";
    }

    @PostMapping("/update")
    public String update(MemoForm form, Principal principal) {
        Memo memo = form.toEntity(principal.getName());
        Memo target = memoService.findById(memo.getId());
        if (target != null) {
            target.patch(memo);
            memoService.save(target);
        }
        return "redirect:/schedules";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        memoService.delete(id);
        return "redirect:/schedules";
    }
}

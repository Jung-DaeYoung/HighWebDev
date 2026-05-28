package com.example.HighWebDev.memo.service;

import com.example.HighWebDev.memo.dto.MemoResponseDto;
import com.example.HighWebDev.memo.entity.Memo;
import com.example.HighWebDev.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public List<MemoResponseDto> findAllByUsername(String username) {
        return memoRepository.findAllByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(MemoResponseDto::new)
                .toList();
    }

    public Memo findById(Long id) {
        return memoRepository.findById(id).orElse(null);
    }

    public Memo save(Memo memo) {
        return memoRepository.save(memo);
    }

    public void delete(Long id) {
        memoRepository.deleteById(id);
    }
}

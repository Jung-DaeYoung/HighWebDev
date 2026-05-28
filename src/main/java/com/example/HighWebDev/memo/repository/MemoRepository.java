package com.example.HighWebDev.memo.repository;

import com.example.HighWebDev.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByUsernameOrderByCreatedAtDesc(String username);
}

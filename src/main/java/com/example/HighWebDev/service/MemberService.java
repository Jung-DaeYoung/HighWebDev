package com.example.HighWebDev.service;

import com.example.HighWebDev.entity.Member;
import com.example.HighWebDev.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(String username, String password) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        Member member = new Member(null, username, passwordEncoder.encode(password), "ROLE_USER");
        memberRepository.save(member);
    }

    // Spring Security 로그인 시 자동 호출
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        return new org.springframework.security.core.userdetails.User(
                member.getUsername(),
                member.getPassword(),
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(member.getRole()))
        );
    }
}
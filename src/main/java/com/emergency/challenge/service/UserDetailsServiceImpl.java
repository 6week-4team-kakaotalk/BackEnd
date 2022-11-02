package com.emergency.challenge.service;

import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        // nickname 이 아닌 emailid 로 변경되었으니 변수명 및 jpa 함수명 변경
        Optional<Member> member = memberRepository.findByLoginId(nickname);

//======================================================================
        System.out.println("멤버 잘 찾아오니 : " + member);

        return member
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}

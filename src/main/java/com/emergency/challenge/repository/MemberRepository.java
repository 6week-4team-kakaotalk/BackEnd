package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(Long id);
    Optional<Member> findByLoginId(String loginId);

}

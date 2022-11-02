package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(Long id);
    @Query(value ="select u from Member u where u.loginId= ?1 ")
    Optional<Member> findByLoginId(String loginId);



}

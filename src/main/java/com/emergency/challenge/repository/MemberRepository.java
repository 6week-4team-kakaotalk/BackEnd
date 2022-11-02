package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(Long id);
//    @Query("select u from Member u where u.memberId like %?1")
    Optional<Member> findByLoginId(String loginId);

//    Optional<Member> findByPhoneNumber(String phoneNumber);

//    Optional<Member> findByEmail(String loginId);

}

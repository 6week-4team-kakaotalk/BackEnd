package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);

   Optional<RefreshToken> findByValue(String value);

}
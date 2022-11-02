package com.emergency.challenge.repository;

import com.emergency.challenge.domain.Friend;
import com.emergency.challenge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("select f from Friend f where f.member = ?1 and f.friend = ?2")
    Optional<Friend> findByMemberAndFriend(Member member, Member friend);

}

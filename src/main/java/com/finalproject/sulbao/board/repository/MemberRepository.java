package com.finalproject.sulbao.board.repository;

import com.finalproject.sulbao.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

package com.kkk.sbgtest.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository {//extends JpaRepository<Member, Long> {
    Member findMemberByName(String name);

    @Query("select m from Member m where m.name = :name or m.id = :id")
    Member findMemberByName(@Param("name") String memberName, @Param("id") long id);
}

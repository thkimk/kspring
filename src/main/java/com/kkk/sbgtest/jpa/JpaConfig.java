package com.kkk.sbgtest.jpa;

import com.kkk.sbgtest.TestSbgApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JpaConfig {
    @Bean(name = "jpaDs")
    @Primary
    @ConfigurationProperties(prefix = "spring.jpa-ds.datasource")
    public DataSource jpaDs() {
        return DataSourceBuilder.create().build();
    }

    //    public static Logger logger = LoggerFactory.getLogger(JpaConfig.class);

//    @Bean
    public CommandLineRunner Member(MemberRepository repository) {
        return (args) -> {
/*
            //엔티티 생성
            Member member1 = new Member("홍길동");
            Member member2 = new Member("이순신");
            Member member3 = new Member("김철수");

            //엔티티 저장
            repository.save(member1);
            repository.save(member2);
            repository.save(member3);

            Member m1 = repository.findMemberByName("홍길동");
            logger.info("검색 된 회원 : {}", m1.getName());

            */
/* 페이징 지원(JPA) *//*

            PageRequest pagerequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
            Page<Member> m2 = repository.findAll(pagerequest);
            logger.info("페이지 건수 : {}", m2.getTotalPages());
            logger.info("다음 페이지 존재 여부 : {}", m2.hasNext());
            List<Member> members = m2.getContent();
            for (Member m : members) {
                logger.info("회원 명 : {}", m.getName());
            }
*/
/*
            //엔티티 조회
            for(Member m : repository.findAll()) {
                logger.info("회원 명 : {}", m.getName());
            }

            //엔티티 삭제
            repository.delete(member2);

            for(Member m : repository.findAll()) {
                logger.info("회원 명 : {}", m.getName());
            }
*/
        };
    }
}

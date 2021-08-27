package com.kkk.sbgtest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // 객체를 serialize할 때 객체가 비어있으면 실패하는 기능과 timestamp로 작성하는 기능을 비활성화,
        // 그리고 JavaTimeModule을 활성화한 ObjectMapper를 스프링 부트 어플리케이션에서 사용할 수 있도록 설정
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }
}


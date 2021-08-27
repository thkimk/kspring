package com.kkk.sbgtest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

// 사용자 정보를 저장하기 위한 빈 : 객체를 serialize
@NoArgsConstructor
@Setter
public class User implements Serializable {

    @Getter
    private String username;

    @Getter
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}

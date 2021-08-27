package com.kkk.sbgtest.model;

import lombok.*;

// @Data: 한번에 어노테이션 설정 (Getter, Setter, ToString, RequiredArgsConstructor, EqualsAndHashCode)
@Data
public class TestLbData {
    private @NonNull String name;
    private @NonNull String age;
    private @NonNull String address;

}

/*
 * @NoArgsConstructor : 파라미터 없는 기본 생성자
 * @AllArgsConstructor : 모든 필드를 파라미터로 받는 생성자
 * @RequiredArgcConstructor : @NonNull/final 필드만 파라미터로 받는 생성자
 * @ToString(exclude="str")
 * @NonNull
 * @EualsAndHashCode : equals(값 비교)와 hashCode(주소 비교)를 자동생성
 */

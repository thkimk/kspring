package com.kkk.sbgtest.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//@Data                   //lombok
//@NoArgsConstructor      //lombok
//@AllArgsConstructor     //lombok
//@Entity
//@Table(name = "member")
public class Member {
//    @Id
//    @GeneratedValue
    private long id;

    private String name;

    public Member(String name) {
        this.name = name;
    }
}

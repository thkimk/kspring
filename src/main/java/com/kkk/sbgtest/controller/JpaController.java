package com.kkk.sbgtest.controller;

import com.kkk.sbgtest.jpa.AirRepository;
import com.kkk.sbgtest.jpa.AirVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class JpaController {
    @Autowired
    private AirRepository airRepository;

    @GetMapping("/jpa1")
    public void jpa1() {
        System.out.println("## jpa1() : begins..");
        try {
            for (int i=0; i<10; i++) {
                AirVO lVo = new AirVO(1L, "aaa"+i);
                AirVO lRes = airRepository.save(lVo);
                System.out.println("## jpa1() : " + lRes.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

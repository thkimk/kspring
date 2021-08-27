package com.kkk.sbgtest.controller;

import com.kkk.sbgtest.dao.Test2Dao;
import com.kkk.sbgtest.dao.TestDao;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DsController {
    @Autowired
    TestDao lDao;

    @Autowired
    Test2Dao lDao2;

    public DsController() {
    }

    @GetMapping("/ds1")
    public void ds1() {
        System.out.println("## ds1() : begins..");
        try {
            ArrayList<HashMap> lRes = lDao.test1();
            System.out.println("## ds1() : " + lRes.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/ds2")
    public void ds2() {
        System.out.println("## ds2() : begins..");
        try {
            ArrayList<HashMap> lRes = lDao2.test1();
            System.out.println("## ds2() : " + lRes.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

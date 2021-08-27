package com.kkk.sbgtest.controller;

import com.kkk.sbgtest.jpa.AirRepository;
import com.kkk.sbgtest.jpa.AirVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/api")
public class SecurityController {

    @GetMapping("/sec")
    public void sec1(HttpSession session, HttpServletRequest httpServletRequest) {
        System.out.println(session.getId());
    }
}

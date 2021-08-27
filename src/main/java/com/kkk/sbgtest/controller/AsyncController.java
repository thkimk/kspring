package com.kkk.sbgtest.controller;

import com.kkk.sbgtest.service.AsyncService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    Logger logger = LoggerFactory.getLogger(AsyncController.class);

    @Autowired
    AsyncService asyncService;

    @Autowired
    private AsyncService service;


    // 클래스 멤버로 하면 Async 동작하는데, 로컬변수로 하면 안됨...
    @ApiOperation(value = "goAsync", notes = "notes")
    @GetMapping("/async")
    public String goAsync() {
//        service.onAsync();
        String str = "Hello Spring Boot Async!!";
//        logger.info(str);
//        logger.info("==================================");
        try {
            AsyncService lAsync = new AsyncService();
            System.out.println("## TestControllerTest.java [asyncTest] -- 1");
            service.onAsync();
            System.out.println("## TestControllerTest.java [asyncTest] -- 2");
        }
        catch (Exception e) {}
        return str;
    }

    @GetMapping("/sync")
    public String goSync() {
        service.onSync();
        String str = "Hello Spring Boot Sync!!";
        logger.info(str);
        logger.info("==================================");
        return str;
    }

}
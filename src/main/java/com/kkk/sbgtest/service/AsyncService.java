package com.kkk.sbgtest.service;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AsyncService {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Async
//    public void async1() {
//        try {
//            Thread.sleep(5000);
//            logger.warn("## TestController.java [getTest3] -- 1.55555");
//        }
//        catch (Exception e) {}
//    }
//}
//package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    //비동기로 동작하는 메소드
    @Async
    public void onAsync() {
        try {
            Thread.sleep(5000);
            logger.info("onAsync");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //동기로 동작하는 메소드
    public void onSync() {
        try {
            Thread.sleep(5000);
            logger.info("onSync");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
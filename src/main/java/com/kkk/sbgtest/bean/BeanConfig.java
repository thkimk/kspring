package com.kkk.sbgtest.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
//    @Autowired --> @Component로 등록된 객체만
    BookService bookService;

    public BeanConfig() {
//        System.out.println("## BeanConfig...... "+ bookService.toString());
    }
}

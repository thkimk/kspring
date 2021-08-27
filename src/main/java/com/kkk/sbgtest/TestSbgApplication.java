package com.kkk.sbgtest;

import com.kkk.sbgtest.bean.BookService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
@EnableCaching
public class TestSbgApplication {
    public static Logger logger = LoggerFactory.getLogger(TestSbgApplication.class);


    public static void main(String[] args) throws ParseException {
        /* 유니코드 */
//        logger.warn("\uc11c\ube44\uc2a4\ub97c \ucc98\ub9ac\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. \ud504\ub860\ud2b8\uc5d0 \ubb38\uc758\ud574\uc8fc\uc138\uc694.");

        /* MDC에 데이터를 저장하니, 로그로 출력되네.. */
//        MDC.put("userid", "terrycho");
//        MDC.put("event", "orderProduct");
//        MDC.put("transactionId", "a123");
//        logger.warn("mdc test");

        /* TestUtils 테스트 : 시간과 날짜 */
//        TestUtils lUtils = new TestUtils();
//        lUtils.utDatetime();

        ApplicationContext context = new ClassPathXmlApplicationContext("beans-config.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));

        BookService bookService = (BookService) context.getBean("bookService");
        System.out.println("BookService bean : "+ bookService.toString());

        /* Main Application */
        SpringApplication.run(TestSbgApplication.class, args);
    }

}

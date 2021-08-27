package com.kkk.sbgtest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TestUtils {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    RestTemplate restTemplate = new RestTemplate();

    /**
     * 전체 초기화 메소드 (Filter의 init보다는 뒤)
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        logger.info("## TestUtils.java [init] PostConstruct ##########################");
    }

    /**
     * 날짜와 시간
     */
    public void utDatetime() throws ParseException {
        // 시스템 시간 출력
        long systemTime	= System.currentTimeMillis();
        logger.info("## TestUtils.java [utDatetime] systemTime : "+ systemTime);

        // 형식과 출력
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String time1 = format1.format(time);
        logger.info("## TestUtils.java [utDatetime] "+ time1);

        // 비교
        String str1 = "2018.09.28 00:30";
        String str2 = "2018.09.30 23:30";

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date date1 = format2.parse(str1);
        Date date2 = format2.parse(str2);

        if(date1.getTime() > date2.getTime()) logger.info("## TestUtils.java [utDatetime] date1이 더 최신입니다..");
        else logger.info("## TestUtils.java [utDatetime] date2가 더 최신입니다..");

        // 연월일, 시분초 분류
        Calendar cal = Calendar.getInstance();
        logger.info("## TestUtils.java [utDatetime] Calendar : "+ cal);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        logger.info("## TestUtils.java [utDatetime] Calendar : "+ year+ ","+ month+ ","+ day+ ","+ hour+ ","+ min+ ","+ sec);
    }

    /**
     * Restful API 호출
     */
    public String utRestApi() {
        try {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(5000);
            requestFactory.setReadTimeout(5000);
            restTemplate.setRequestFactory(requestFactory);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            HttpEntity entity = new HttpEntity("{\"id\":\"thkim\"}", headers);

            String url = "http://localhost:8090/api/postMapping";

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            logger.info("## TestUtils.java [utRestApi] "+ response.toString());
        }
        catch (Exception e) {
            throw e;
        }
        return "test";
    }

    public ResponseEntity<String> sendPostByJson(String url, Map headersMap, Map bodyMap) throws HttpClientErrorException, RuntimeException  {
        ResponseEntity<String> response = null;
        try {
            // 호출 설정
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(5000);
            requestFactory.setReadTimeout(5000);
            restTemplate.setRequestFactory(requestFactory);

            // 헤더+바디 생성
            String body = "";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
            headers.add("x-api-key", "aaaabbbbccccdddd");
            HttpEntity entity = new HttpEntity(body, headers);

            // 호출 = URL + entity(헤더+바디)
            response = restTemplate.postForEntity(url, entity, String.class);
        }
        catch (Exception e) {
            throw e;
        }

        return response;
    }

}

package com.kkk.sbgtest;

import com.kkk.sbgtest.model.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@EnableScheduling
public class TestBatch {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${test.base}")
    private String testBase;

    /**
     * KT API를 연동받는, 외부 서버의 인증토큰 얻어오기
     * 매일 00시 10분에 수행
     * 차후에 해당 서비스를 강제로 실행할 수 있는 컨트롤러 필요
     */
    //@Scheduled(initialDelay = 5000, fixedDelay = 7000)  // 10초의 딜레이 후에, 7초마다 수행
    //@Scheduled(cron="0 0 12 * * *") // 매일 오후 12시 0분에 수행
    public void doBatch() {
        logger.info("************* doBatch *************** "+ testBase);
        //batch_service.doExternalAuth();

        /* function IN/OUT parameter #1 */
/* 이렇게 하면, user1.이 null로 죽어버림
        User user1 = null;
        copyData1(user1);
        logger.info("## TestBatch.java [doBatch] "+ user1.getUsername());
*/
        User user1 = new User();
        copyData1(user1);
        logger.info("## TestBatch.java [doBatch] "+ user1.getUsername());

    }


    /**
     * 웹 크롤링 처리
     * 주기적으로 웹크롤링 데이터 현행화 하기
     */
    //@Scheduled(initialDelay = 5000, fixedDelay = 7000)  // 10초의 딜레이 후에, 7초마다 수행
    //@Scheduled(cron="0 0 12 * * *") // 매일 오후 12시 0분에 수행
    public void doCrawling() {
        logger.info("************* doCrawling *************** ");

        try {
            Document doc = Jsoup.connect("http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13").get();
            Elements contents = doc.select("table tbody tr");

            for (Element content : contents) {
                Elements tdContents = content.select("td");
                logger.info("## TestBatch.java [doCrawling] td : " + tdContents.toString());

//                KoreaStats koreaStats = KoreaStats.builder()
//                        .country(content.select("th").text())
//                        .diffFromPrevDay(Integer.parseInt(tdContents.get(0).text()))
//                        .total(Integer.parseInt(tdContents.get(1).text()))
//                        .death(Integer.parseInt(tdContents.get(2).text()))
//                        .incidence(Double.parseDouble(tdContents.get(3).text()))
//                        .inspection(Integer.parseInt(tdContents.get(4).text()))
//                        .build();

                //logger.info("## TestBatch.java [doCrawling] " + koreaStats.toString());
            }
        }
        catch (IOException e) {

        }

    }


    private void copyData1(User user) {
        user.setUsername("thkim");
        logger.info("## TestBatch.java [copyData1] "+ user.getUsername());
    }

}
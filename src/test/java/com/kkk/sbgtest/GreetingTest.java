package com.kkk.sbgtest;

import com.kkk.sbgtest.common.Constants;
import com.kkk.sbgtest.mongodb.MongoMyService;
import com.kkk.sbgtest.stock.StockData;
import com.kkk.sbgtest.stock.StockDatas;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class GreetingTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MongoMyService mongoMyService;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("==== Before this class ====\n");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("==== After this Class ====\n");
    }

    @Before
    public void before() {
        System.out.println("==== Before every test ====");
    }

    @After
    public void after() {
        System.out.println("==== After every test ====\n");
    }

    @Test
    public void dummyTest() {
        System.out.println("Test is testing now...");
    }

    @Test
    public void startTest() {
        System.out.println("Test is testing now...");

        test1();
        test2();
    }

    @Test
    public void test1() {
        System.out.println("Test1 is testing now...");

        String name = "devson";
        TestJunit.greeting("devson");

        assertThat(TestJunit.greeting(name), is("Hello, " + name));
    }

    @Test
    public void test2() {
        System.out.println("Test2 is testing now...");

        String name = "devson";
        TestJunit.greeting(name);

        assertThat(TestJunit.greeting(name), not("hello, " + name));
    }


    @Test
    public void test3() {
        System.out.println("Test3 begins...");
//        int[] A = {1, 3, 6, 5,4, 1, 2};
//        System.out.println("Result : "+ solution(A));

//        System.out.println(solution(3,7));

    }

    public int solution_1(int[] A) {
        // write your code in Java SE

        int lRet = 1;
        int lMin = A[0];
        int lMax = A[0];
        for (int i=0; i<A.length; i++) {
            if (lMin > A[i]) lMin = A[i];
            if (lMax < A[i]) lMax = A[i];
            if (lRet <= A[i]) {
                lRet = A[i]+1;
            }
        }
        if (lMax < 0) return lRet;

        for (int i=lMin; i<=lMax; i++) {
            int j=0;
            for (; j<A.length; j++) {
                if (i == A[j]) break;
            }
            if (j == A.length) return i;
        }
        return lRet;
    }

    public int solution_2(int A, int B) {
        // write your code in Java SE 8
        int lVal = A*B;
        String lValStr = Integer.toBinaryString(lVal);
        char[] lValChars = lValStr.toCharArray();
        int lCount = 0;
        for (int i=0; i<lValStr.length(); i++) {
            if (lValChars[i] == '1') lCount++;
        }
        return lCount;
    }

    @Test
    public void collectStocks() {
        StockDatas lDatas = new StockDatas();
        lDatas.collectCrawlings();
    }
    @Test
    public void scoreStocks2() {
        StockDatas lStockDatas = StockDatas.serialFromFile();
        lStockDatas.collectDatas();
        lStockDatas.score3();

        // 점수(Score) 매기기 (점수가 가장 높은 종목순으로 소팅)
        StockDatas lStockDatas2 = StockDatas.serialFromFile();
        lStockDatas2.collectDatas();
        lStockDatas2.score4();
        logger.info("#### Completed..");

        // 기업개요

    }

    @Test
    public void scoreStocks() {
        StockDatas lStockDatas = StockDatas.serialFromFile();
        lStockDatas.collectDatas();
        lStockDatas.score9();

/*
        // 점수(Score) 매기기 (점수가 가장 높은 종목순으로 소팅)
        StockDatas lStockDatas2 = StockDatas.serialFromFile();
        lStockDatas2.collectDatas();
        lStockDatas2.score7();
*/
        logger.info("#### Completed..");

        // 기업개요

    }

    @Test
    public void scoreStocks_old() {
        StockDatas lTmp = new StockDatas();
        StockDatas lStockDatas = lTmp.serialFromFile();
        List<StockData> lDatas = lStockDatas.getDatas();

        lStockDatas.collectDatas();

        // 점수(Score) 매기기 (점수가 가장 높은 종목순으로 소팅)
        lStockDatas.score();
        logger.info("#### Completed..");

        // 기업개요

    }

    @Test
    public void testInfo() {
        String lBaseUrl = "http://finance.naver.com/item/coinfo.nhn?";

        try {
            String lUrl = lBaseUrl + "code=033250";
            Document doc = Jsoup.connect(lUrl).get();
            Elements lElems = doc.select("#summary_info");
            Elements lElems2 = lElems.select("p");
            for (Element elem : lElems2) {
                System.out.println(elem.outerHtml());
            }

            logger.info("#### Completed..");
        } catch (Exception e) {
            logger.info("#### Error..");
        }
    }


    @Test
    public void testBoard() {
        String lBaseUrl = "http://finance.naver.com/item/board.nhn?";

        try {
            String lUrl = lBaseUrl + "code=010660";
            Document doc = Jsoup.connect(lUrl).get();
            Elements lElems = doc.select(".section");
            String lStr = lElems.toString();
            boolean a= lStr.contains("뜬금없이");

            logger.info("#### Completed..");
        } catch (Exception e) {
            logger.info("#### Error..");
        }
    }


    @Test
    public void testPER() {
        String lBaseUrl = "https://navercomp.wisereport.co.kr/v2/company/c1010001.aspx?cmp_cd=";

        try {
            String lUrl = lBaseUrl + "000270";
            Document doc = Jsoup.connect(lUrl).get();
            Elements lElems = doc.select("td.cmp-table-cell.td0301").select("dl").select("dt");
            for (Element lElem : lElems) {
                String lOwnText = lElem.ownText();
                if (lOwnText.equals("PER")) {
                    System.out.println("PER: "+ lElem.select("b").get(0).text());
                } else if (lOwnText.equals("업종PER")) {
                    System.out.println("업종PER: "+ lElem.select("b").get(0).text());
                }
//                String lTmp = lElem.data();
//                Elements lEs = lElem.children();
//                lTmp = lElem.text();
//                Node lNode = lElem.childNode(0);
//
//                System.out.println(lElem.outerHtml());
            }
            logger.info("#### Completed..");
        } catch (Exception e) {
            logger.info("#### Error..");
        }
    }


    @Test
    public void testMongodb() {
        mongoMyService.testMongo();
    }
}
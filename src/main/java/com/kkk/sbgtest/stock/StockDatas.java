package com.kkk.sbgtest.stock;

import com.kkk.sbgtest.common.Constants;
import lombok.Data;
import org.apache.commons.lang.SerializationUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class StockDatas implements Serializable {
    private static final long serialVersionUID = -4865946674835353965L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    List<StockData> datas = new ArrayList<>();

    // 추천종목 10건 추출 (스코어 계산 후, 스코어로 상위 10건 추출)

    public void serialToFile() {
        try {
            // 파일 직렬화
            Constants.initStockFile();
            FileOutputStream fileStream = new FileOutputStream(Constants.STOCK_FILE);
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(this);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StockDatas serialFromFile() {
        StockDatas lRet = null;
        try {
            Constants.initStockFile();
            FileInputStream fileStream = new FileInputStream(Constants.STOCK_FILE);
            ObjectInputStream os = new ObjectInputStream(fileStream);
            lRet = (StockDatas) os.readObject();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lRet;
    }

    public void collectCrawlings() {
        logger.info("************* doCrawling *************** ");
        String lBaseUrl = "http://finance.naver.com/item/sise_day.nhn?";

        try {
            // 종목 리스트(엑셀)
            StockItems lStockItems = new StockItems();
            List<StockItem> lItems = lStockItems.getItems();

            for (int i=0; i < lItems.size(); i++) {
                StockItem lITem = lItems.get(i);
                StockData lData = new StockData(lITem);
                logger.info("## [testStock] for[{}] {} / {}", i+1, lData.getItem().getCode(), lData.getItem().getName());

                // 1st Basic Data
                // 10개 데이터를 30개 데이터로 늘려 (x3회 looping)
                Elements lAllTrs = new Elements();
                for (int j = 0; j <= Constants.CRAWLING_COUNT; j++) {
                    // 웹크롤링 수행
                    String lUrl = lBaseUrl+ "code="+ lITem.getCode()+ "&page="+ (j+1);
                    Document doc = Jsoup.connect(lUrl).get();
//                    logger.info("## [testStock] Document \n" + doc.toString());

                    // 복수 데이터 추출
                    Elements lTrs = doc.select("tr[onmouseover^='mo']");
//                    logger.info("## [testStock] Elements \n" + lTrs.toString());
                    if (lTrs.size() != 10) {
                        logger.error("## [testStock] crawlling size in Not 10..");
                        throw new Exception();
                    }

                    lAllTrs.addAll(lTrs);
                }

                // 1st/Raw Data
                lData.setCrawlingData(lAllTrs);
                lData.setId(i+1);

                // 1개 종목 완성
                datas.add(lData);
                Thread.sleep(1000);
            }
            serialToFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void collectDatas() {
        for (StockData lData : datas) {
            lData.setAllData();
            lData.setAllData2nd();
        }
    }

    public void score() {
        // rankWidthValue;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 클수록 좋아 : d1이 더 크면 -1? -1은 앞으로가 --> 앞으로 갈수록 더큰값으로 정렬됨
                if (d1.getWidthValue() == d2.getWidthValue()) return 0;
                return d1.getWidthValue() > d2.getWidthValue()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankWidthValue(i+1);
        }

        // rankCurrValuePos;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 작을수록 좋아 : d1이 더 크면 1? 1은 뒤로가 --> 앞으로 갈수록 더작은값으로 정렬됨
                if (d1.getCurrValuePos() == d2.getCurrValuePos()) return 0;
                return d1.getCurrValuePos() > d2.getCurrValuePos()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankCurrValuePos(i+1);
        }

        // rankRatioHL;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL() == d2.getRatioHL()) return 0;
                return d1.getRatioHL() > d2.getRatioHL()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHL(i+1);
        }

        // rankRatioCC;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
//                if (Float.isNaN(d1.getRatioCC()) || Float.isNaN(d2.getRatioCC())) return -1;
                if (d1.getRatioCC() == d2.getRatioCC()) return 0;
                return d1.getRatioCC() > d2.getRatioCC()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC(i+1);
        }

        // rankRatioCO;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
//                if (Float.isNaN(d1.getRatioCO()) || Float.isNaN(d2.getRatioCO())) return -1;
                if (d1.getRatioCO() == d2.getRatioCO()) return 0;
                return d1.getRatioCO() > d2.getRatioCO()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCO(i+1);
        }

        // rankRatioVV;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV() == d2.getRatioVV()) return 0;
                return d1.getRatioVV() > d2.getRatioVV()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV(i+1);
        }

        // rankRatioOL;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 클수록 좋아 : d1이 더 크면 -1? -1은 앞으로가 --> 앞으로 갈수록 더큰값으로 정렬됨
                if (d1.getRatioOL() == d2.getRatioOL()) return 0;
                return d1.getRatioOL() > d2.getRatioOL()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL(i+1);
        }

        // rankRatioHL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL2() == d2.getRatioHL2()) return 0;
                return d1.getRatioHL2() > d2.getRatioHL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHL2(i+1);
        }

        // rankRatioCO2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCO2() == d2.getRatioCO2()) return 0;
                return d1.getRatioCO2() > d2.getRatioCO2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCO2(i+1);
        }

        // rankRatioOL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL2() == d2.getRatioOL2()) return 0;
                return d1.getRatioOL2() > d2.getRatioOL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL2(i+1);
        }

        // rankRatioVV2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV2() == d2.getRatioVV2()) return 0;
                return d1.getRatioVV2() > d2.getRatioVV2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV2(i+1);
        }

        // rankRatioCC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC2() == d2.getRatioCC2()) return 0;
                return d1.getRatioCC2() > d2.getRatioCC2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC2(i+1);
        }

        // 보합
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getSteadyValue() == d2.getSteadyValue()) return 0;
                return d1.getSteadyValue() > d2.getSteadyValue()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankSteadyValue(i+1);
        }

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getDescVolume() == d2.getDescVolume()) return 0;
                return d1.getDescVolume() > d2.getDescVolume()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankDescVolume(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });
        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = Constants.SCORE_SEL_COUNT;
        for (int i=0; i< Constants.SCORE_SEL_COUNT2; i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (lData.isSkip) continue;
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                    loopCount++;
                } else if (lData.isStop()) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                    loopCount++;
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 1st loop : 일변화가 큰것 추출 (상위 100건)
     * 2nd loop : 진폭이 큰것 추출 (상위 20건)
     * 3rd loop : 10일부터 서서히 상승 (상위 5건)
     */
    public void score2() {
        // 하루중 매수와 매도시기를 결정
        int isUp = 0;
        for (StockData lData: datas) {
            if (lData.isUp()) isUp++;
            else isUp--;
        }
        logger.info("#### [주가 방향성] isUp : {}, {}", isUp, isUp>0? "가격이 올라갑니다": "가격이 떨어집니다");

        // rankRatioHL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL2() == d2.getRatioHL2()) return 0;
                return d1.getRatioHL2() > d2.getRatioHL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHL2(i+1);
        }

        // rankRatioCO2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCO2() == d2.getRatioCO2()) return 0;
                return d1.getRatioCO2() > d2.getRatioCO2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCO2(i+1);
        }

        // rankRatioOL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL2() == d2.getRatioOL2()) return 0;
                return d1.getRatioOL2() > d2.getRatioOL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL2(i+1);
        }

        // rankRatioVV2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV2() == d2.getRatioVV2()) return 0;
                return d1.getRatioVV2() > d2.getRatioVV2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV2(i+1);
        }

        // rankRatioVV3;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV3() == d2.getRatioVV3()) return 0;
                return d1.getRatioVV3() > d2.getRatioVV3()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV3(i+1);
        }

        // rankRatioCC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC2() == d2.getRatioCC2()) return 0;
                return d1.getRatioCC2() > d2.getRatioCC2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC2(i+1);
        }

        // rankRatioLC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioLC2() == d2.getRatioLC2()) return 0;
                return d1.getRatioLC2() > d2.getRatioLC2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioLC2(i+1);
        }

        // rankRatioHC;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHC() == d2.getRatioHC()) return 0;
                return d1.getRatioHC() > d2.getRatioHC()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHC(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore2();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        int lSize = datas.size();
        for (int i=80; i<lSize; i++) {
            datas.remove(80);
        }

        /*** 2nd ranking ***/

        // rankCurrValuePos2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 작을수록 좋아 : d1이 더 크면 1? 1은 뒤로가 --> 앞으로 갈수록 더작은값으로 정렬됨
                if (d1.getCurrValuePos10() == d2.getCurrValuePos10()) return 0;
                return d1.getCurrValuePos10() > d2.getCurrValuePos10()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankCurrValuePos10(i+1);
        }

        lSize = datas.size();
        for (int i=20; i<lSize; i++) {
            datas.remove(20);
        }

        /*** 3rd ranking ***/

/*
        // rankWidthValue;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 클수록 좋아 : d1이 더 크면 -1? -1은 앞으로가 --> 앞으로 갈수록 더큰값으로 정렬됨
                if (d1.getWidthValue() == d2.getWidthValue()) return 0;
                return d1.getWidthValue() > d2.getWidthValue()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankWidthValue(i+1);
        }
*/

        // rankCurrValuePos;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 작을수록 좋아 : d1이 더 크면 1? 1은 뒤로가 --> 앞으로 갈수록 더작은값으로 정렬됨
                if (d1.getCurrValuePos() == d2.getCurrValuePos()) return 0;
                return d1.getCurrValuePos() > d2.getCurrValuePos()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankCurrValuePos(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore3();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = 5;
        for (int i=0; i< 20; i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                    loopCount++;
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                    loopCount++;
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                    loopCount++;
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void score4() {
        int lSize = datas.size();
        for (int i = 0; i < lSize; i++) {
            StockData lData = datas.get(i);
            if (!lData.isFlag1()) {
                datas.remove(i);
                lSize--;
                i--;
            }
        }

        /*** 3rd ranking ***/
        // rankWidthValue;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 클수록 좋아 : d1이 더 크면 -1? -1은 앞으로가 --> 앞으로 갈수록 더큰값으로 정렬됨
                if (d1.getWidthValue() == d2.getWidthValue()) return 0;
                return d1.getWidthValue() > d2.getWidthValue()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankWidthValue(i+1);
        }

        // rankCurrValuePos;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 작을수록 좋아 : d1이 더 크면 1? 1은 뒤로가 --> 앞으로 갈수록 더작은값으로 정렬됨
                if (d1.getCurrValuePos() == d2.getCurrValuePos()) return 0;
                return d1.getCurrValuePos() > d2.getCurrValuePos()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankCurrValuePos(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore3();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE1);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void score5() {
        int lSize = datas.size();
        for (int i = 0; i < lSize; i++) {
            StockData lData = datas.get(i);
            if (!lData.isFlagDown()) {
                datas.remove(i);
                lSize--;
                i--;
            }
        }

        lSize = datas.size();
        for (int i = 0; i < lSize; i++) {
            StockData lData = datas.get(i);
            if (!lData.isFlagClose()) {
                datas.remove(i);
                lSize--;
                i--;
            }
        }

        lSize = datas.size();
        for (int i = 0; i < lSize; i++) {
            StockData lData = datas.get(i);
            if (!lData.isFlagVolume()) {
                datas.remove(i);
                lSize--;
                i--;
            }
        }

        /*** 3rd ranking ***/
        // rankRatioOL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL2() == d2.getRatioOL2()) return 0;
                return d1.getRatioOL2() > d2.getRatioOL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL2(i+1);
        }
        // rankRatioLC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioLC2() == d2.getRatioLC2()) return 0;
                return d1.getRatioLC2() > d2.getRatioLC2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioLC2(i+1);
        }
        // rankRatioHL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL2() == d2.getRatioHL2()) return 0;
                return d1.getRatioHL2() > d2.getRatioHL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHL2(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
//            lData.calScore4();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE1);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void score6() {
        /* 1st : ratioCC01, ratioOH01, ratioCC02(작), ratioOL02 */
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.getRatioCC02() > 1.01) {
                datas.remove(i); i--;
            }
        }
        // ratioCC01;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC01() == d2.getRatioCC01()) return 0;
                return d1.getRatioCC01() > d2.getRatioCC01()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC01(i+1);
        }
        // ratioOH01;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOH01() == d2.getRatioOH01()) return 0;
                return d1.getRatioOH01() > d2.getRatioOH01()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOH01(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore01();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        int lSize = datas.size();
        for (int i=25; i<lSize; i++) {
            datas.remove(25);
        }


        // ratioCC02;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC02() == d2.getRatioCC02()) return 0;
                return d1.getRatioCC02() > d2.getRatioCC02()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC02(i+1);
        }
        // ratioOL02;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL02() == d2.getRatioOL02()) return 0;
                return d1.getRatioOL02() > d2.getRatioOL02()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL02(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore02();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            int k=0;
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                    if (++k == 5) break;
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void score7() {
        /* 1st : ratioCC11(작), ratioHL11(작), ratioCC12, ratioHL12 */
        // ratioCC11;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC11() == d2.getRatioCC11()) return 0;
                return d1.getRatioCC11() > d2.getRatioCC11()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC11(i+1);
        }
        // ratioHL11;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL11() == d2.getRatioHL11()) return 0;
                return d1.getRatioHL11() > d2.getRatioHL11()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHL11(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore11();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        int lSize = datas.size();
        for (int i=50; i<lSize; i++) {
            datas.remove(50);
        }


        // ratioCC12;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC12() == d2.getRatioCC12()) return 0;
                return d1.getRatioCC12() > d2.getRatioCC12()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC12(i+1);
        }
        // ratioHL12;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL12() == d2.getRatioHL12()) return 0;
                return d1.getRatioHL12() > d2.getRatioHL12()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHL12(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore12();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE1);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            int k=0;
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                    if (++k == 5) break;
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void score8() {
        /* 1st : ratioCC11(작), ratioHL11(작), ratioCC12, ratioHL12 */
        // ratioCC11;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV3() == d2.getRatioVV3()) return 0;
                return d1.getRatioVV3() > d2.getRatioVV3()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV3(i+1);
        }

//        int lSize = datas.size();
//        for (int i=10; i<lSize; i++) {
//            datas.remove(10);
//        }

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            int k=0;
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                    if (++k == 5) break;
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void preScore() {
        int lSize;

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getMinVolume() == d2.getMinVolume()) return 0;
                return d1.getMinVolume() > d2.getMinVolume()? 1: -1;
            }
        });
        lSize = (int)(datas.size()*0.2);
        for (int i=0; i<lSize; i++) {
            datas.remove(0);
        }

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getMinValue() == d2.getMinValue()) return 0;
                return d1.getMinValue() > d2.getMinValue()? 1: -1;
            }
        });
        lSize = (int)(datas.size()*0.1);
        for (int i=0; i<lSize; i++) {
            datas.remove(0);
        }

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getMaxValue() == d2.getMaxValue()) return 0;
                return d1.getMaxValue() > d2.getMaxValue()? -1: 1;
            }
        });
        lSize = (int)(datas.size()*0.1);
        for (int i=0; i<lSize; i++) {
            datas.remove(0);
        }

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHL2() == d2.getRatioHL2()) return 0;
                return d1.getRatioHL2() > d2.getRatioHL2()? 1: -1;
            }
        });
        lSize = (int)(datas.size()*0.1);
        for (int i=0; i<lSize; i++) {
            datas.remove(0);
        }

    }
    public void score9() {
        preScore();

        /*************** #1 : 떨어진다 ******************/
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC22() == d2.getRatioCC22()) return 0;
                return d1.getRatioCC22() > d2.getRatioCC22()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC22(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV22() == d2.getRatioVV22()) return 0;
                return d1.getRatioVV22() > d2.getRatioVV22()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV22(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL22() == d2.getRatioOL22()) return 0;
                return d1.getRatioOL22() > d2.getRatioOL22()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL22(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOH22() == d2.getRatioOH22()) return 0;
                return d1.getRatioOH22() > d2.getRatioOH22()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOH22(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore22();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? 1: -1;
            }
        });

        int lSize = datas.size();
        for (int i=0; i<(lSize-90); i++) {
            datas.remove(0);
        }

        /*************** #2 : 회복할까? ******************/
/*
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioC4C21() == d2.getRatioC4C21()) return 0;
                return d1.getRatioC4C21() > d2.getRatioC4C21()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioC4C21(i+1);
        }

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC01() == d2.getRatioCC01()) return 0;
                return d1.getRatioCC01() > d2.getRatioCC01()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC01(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore24();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? 1: -1;
            }
        });

        lSize = datas.size();
        for (int i=0; i<(lSize-30); i++) {
            datas.remove(0);
        }
*/


        /*************** #3 ******************/
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC23() == d2.getRatioCC23()) return 0;
                return d1.getRatioCC23() > d2.getRatioCC23()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC23(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV23() == d2.getRatioVV23()) return 0;
                return d1.getRatioVV23() > d2.getRatioVV23()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV23(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOH23() == d2.getRatioOH23()) return 0;
                return d1.getRatioOH23() > d2.getRatioOH23()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOH23(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore23();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? 1: -1;
            }
        });
        lSize = datas.size();
        for (int i=0; i<(lSize-30); i++) {
            datas.remove(0);
        }

        /*************** #4 ******************/
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV2() == d2.getRatioVV2()) return 0;
                return d1.getRatioVV2() > d2.getRatioVV2()? 1: -1;
            }
        });


        /*************** #Summary ******************/
        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            int k=0;
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                    if (++k == 7) break;
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void score9_1() {
        /*************** #1 : 떨어진다 ******************/
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC22() == d2.getRatioCC22()) return 0;
                return d1.getRatioCC22() > d2.getRatioCC22()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC22(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV22() == d2.getRatioVV22()) return 0;
                return d1.getRatioVV22() > d2.getRatioVV22()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV22(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL22() == d2.getRatioOL22()) return 0;
                return d1.getRatioOL22() > d2.getRatioOL22()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL22(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOH22() == d2.getRatioOH22()) return 0;
                return d1.getRatioOH22() > d2.getRatioOH22()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOH22(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore22();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        int lSize = datas.size();
        for (int i=90; i<lSize; i++) {
            datas.remove(90);
        }

        /*************** #2 : 회복할까? ******************/
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC23() == d2.getRatioCC23()) return 0;
                return d1.getRatioCC23() > d2.getRatioCC23()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC23(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV23() == d2.getRatioVV23()) return 0;
                return d1.getRatioVV23() > d2.getRatioVV23()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV23(i+1);
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOH23() == d2.getRatioOH23()) return 0;
                return d1.getRatioOH23() > d2.getRatioOH23()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOH23(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore23();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        lSize = datas.size();
        for (int i=30; i<lSize; i++) {
            datas.remove(30);
        }


        /*************** #3 ******************/
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioC4C21() == d2.getRatioC4C21()) return 0;
                return d1.getRatioC4C21() > d2.getRatioC4C21()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioC4C21(i+1);
        }

        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCC01() == d2.getRatioCC01()) return 0;
                return d1.getRatioCC01() > d2.getRatioCC01()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC01(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore24();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });


        /*************** #Summary ******************/
        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            int k=0;
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                    if (++k == 5) break;
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void score711() {
        /* 1st : ratioCC2, ratioVV2, ratioCO2 */
        // ratioCC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                if (d1.getRatioCC2() == d2.getRatioCC2()) return 0;
                return d1.getRatioCC2() > d2.getRatioCC2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC2(i+1);
        }
        // ratioVV2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                if (d1.getRatioVV2() == d2.getRatioVV2()) return 0;
                return d1.getRatioVV2() > d2.getRatioVV2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV2(i+1);
        }

        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore5();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });
        int lSize = datas.size();
        for (int i=50; i<lSize; i++) {
            datas.remove(50);
        }

        // ratioCO2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCO2() == d2.getRatioCO2()) return 0;
                return d1.getRatioCO2() > d2.getRatioCO2()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCO2(i+1);
        }
        lSize = datas.size();
        for (int i=5; i<lSize; i++) {
            datas.remove(5);
        }

        //## 최종 스코어 계산 ##

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = datas.size();
        for (int i=0; i< datas.size(); i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE1);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");

            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void score3() {
        int lSize = datas.size();

        // 하루중 매수와 매도시기를 결정
        int isUp = 0;
        for (StockData lData: datas) {
            if (lData.isUp()) isUp++;
            else isUp--;
        }
        logger.info("#### [주가 방향성] isUp : {}, {}", isUp, isUp>0? "가격이 올라갑니다": "가격이 떨어집니다");

        // rankRatioCO2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioCO2() == d2.getRatioCO2()) return 0;
                return d1.getRatioCO2() > d2.getRatioCO2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCO2(i+1);
        }

        // rankRatioOL2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioOL2() == d2.getRatioOL2()) return 0;
                return d1.getRatioOL2() > d2.getRatioOL2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioOL2(i+1);
        }

        // rankRatioVV2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV2() == d2.getRatioVV2()) return 0;
                return d1.getRatioVV2() > d2.getRatioVV2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV2(i+1);
        }

        // rankRatioVV3;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioVV3() == d2.getRatioVV3()) return 0;
                return d1.getRatioVV3() > d2.getRatioVV3()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioVV3(i+1);
        }

        // rankRatioCC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 작을수록 좋아 : d1이 더 작으면 1, 같으면 0, 크면 -1
                if (d1.getRatioCC2() == d2.getRatioCC2()) return 0;
                return d1.getRatioCC2() > d2.getRatioCC2()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioCC2(i+1);
        }

        // rankRatioLC2;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioLC2() == d2.getRatioLC2()) return 0;
                return d1.getRatioLC2() > d2.getRatioLC2()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioLC2(i+1);
        }

        // rankRatioHC;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioHC() == d2.getRatioHC()) return 0;
                return d1.getRatioHC() > d2.getRatioHC()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioHC(i+1);
        }

        // rankRatioLL;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getRatioLL() == d2.getRatioLL()) return 0;
                return d1.getRatioLL() > d2.getRatioLL()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankRatioLL(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore2();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        lSize = datas.size();
        for (int i=30; i<lSize; i++) {
            datas.remove(30);
        }

        /*** 3rd ranking ***/
        // rankWidthValue;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 클수록 좋아 : d1이 더 크면 -1? -1은 앞으로가 --> 앞으로 갈수록 더큰값으로 정렬됨
                if (d1.getWidthValue() == d2.getWidthValue()) return 0;
                return d1.getWidthValue() > d2.getWidthValue()? -1: 1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankWidthValue(i+1);
        }

        // rankCurrValuePos;
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // 작을수록 좋아 : d1이 더 크면 1? 1은 뒤로가 --> 앞으로 갈수록 더작은값으로 정렬됨
                if (d1.getCurrValuePos() == d2.getCurrValuePos()) return 0;
                return d1.getCurrValuePos() > d2.getCurrValuePos()? 1: -1;
            }
        });
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.setRankCurrValuePos(i+1);
        }

        //## 최종 스코어 계산 ##
        for (int i=0; i<datas.size(); i++) {
            StockData lData = datas.get(i);
            lData.calScore3();
        }
        Collections.sort(datas, new Comparator<StockData>() {
            @Override
            public int compare(StockData d1, StockData d2) {
                // d1이 더 크면 1, 같으면 0, 작으면 -1
                if (d1.getScore() == d2.getScore()) return 0;
                return d1.getScore() > d2.getScore()? -1: 1;
            }
        });

        logger.info("#### "+ Constants.STOCK_FILE);
        int loopCount = 5;
        for (int i=0; i< 20; i++) {
            StockData lData = datas.get(i);
            if (lData.isNew()) continue;
            else if (lData.isStop()) continue;
            else {
                System.out.print(lData.getItem().getName()+ ", ");
            }
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성

            System.out.print("\n\n");
            for (int i=0, j=0; i < loopCount; i++) {
                StockData lData = datas.get(i);
                if (j == 0) lData.writeExcelHeader(sheet.createRow(j++));

                if (lData.isNew()) {
                    logger.info("#### [{}] {} : Score {} - 신규상장 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsNewDate());
                    loopCount++;
                } else if (lData.isStop) {
                    logger.info("#### [{}] {} : Score {} - 거래정지 {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                    loopCount++;
                } else if (lData.isSkip) {
                    logger.info("#### [{}] {} : Score {} - isSkip {}", i + 1, lData.getItem().getName(), lData.getScore(), lData.getIsStopDate());
                    loopCount++;
                } else {
                    logger.info("#### [{}] {}({}) : Score {} - 현재/최소 거래량 {}/{}", i + 1, lData.getItem().getName(), lData.getId(), lData.getScore(), lData.getCurrVolume(), lData.getMinVolume());
                    String lPer = printPER(lData.getItem().getCode());
                    String lBoard = printBoardInfo(lData.getItem().getCode());
                    String lCo = printCoInfo(lData.getItem().getCode());

                    lData.writeExcelRow(sheet.createRow(j++), lPer, lBoard, lCo);
                }
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    String printCoInfo(String code) {
        String lBaseUrl = "http://finance.naver.com/item/coinfo.nhn?";
        String lRet = "";

        try {
            String lUrl = lBaseUrl+ "code="+ code;
            Document doc = Jsoup.connect(lUrl).get();
            Elements lElems = doc.select("#summary_info");
            Elements lElems2 = lElems.select("p");
            for (Element elem : lElems2) {
                System.out.println("    "+ elem.outerHtml());
                lRet += elem.outerHtml()+ "\n";
            }
        } catch (Exception e) {
            logger.info("#### Error..");
        }

        return lRet;
    }

    String printBoardInfo(String code) {
        String lBaseUrl = "http://finance.naver.com/item/board.nhn?";
        String lRet = "";

        try {
            String lUrl = lBaseUrl+ "code="+ code;
            Document doc = Jsoup.connect(lUrl).get();
            Elements lElem = doc.select(".section");
            String lElemStr = lElem.toString();
            String lOut = null;
            for (String wording : Constants.BOARD_WORDINGS) {
                boolean a = lElemStr.contains(wording);
                if (a) {
                    if (lOut == null) lOut = "";
                    lOut += wording+ ", ";
                }
            }
            if (lOut != null) System.out.println("    [게시판] "+ lOut);
            lRet = lOut;
        } catch (Exception e) {
            logger.info("#### Error..");
        }

        return lRet;
    }


    String printPER(String code) {
        String lBaseUrl = "https://navercomp.wisereport.co.kr/v2/company/c1010001.aspx?cmp_cd=";
        String lRet = "";

        try {
            String lUrl = lBaseUrl + code;
            Document doc = Jsoup.connect(lUrl).get();
            Elements lElems = doc.select("td.cmp-table-cell.td0301").select("dl").select("dt");
            float lPer = 0;
            float lGenPer = 0;
            for (Element lElem : lElems) {
                String lOwnText = lElem.ownText();
                if (lOwnText.equals("PER")) {
                    String lVal = lElem.select("b").get(0).text();
                    if (lVal.equals("N/A")) lPer = 100;
                    else lPer = Float.valueOf(lVal);
                } else if (lOwnText.equals("업종PER")) {
                    String lVal = lElem.select("b").get(0).text();
                    if (lVal.equals("N/A")) lGenPer = -100;
                    else lGenPer = Float.valueOf(lVal);
                }
            }

            lRet = String.valueOf(lGenPer - lPer);
            System.out.println("    [저평가] "+ lRet);
        } catch (Exception e) {
            logger.info("#### Error..");
        }

        return lRet;
    }

    void writeExcel() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(Constants.STOCK_RESULT_FILE);
            XSSFSheet sheet = workbook.createSheet("result");    // sheet 생성
            for (int i=0; i<datas.size(); i++) {
                StockData lData = datas.get(i);
                if (i == 0) lData.writeExcelHeader(sheet.createRow(i));

//                lData.writeExcelRow(sheet.createRow(i+1));
            }

            workbook.write(fos);
            fos.close();
        } catch (Exception e) {}
    }

}

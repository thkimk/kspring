package com.kkk.sbgtest.stock;

import com.kkk.sbgtest.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class StockData implements Serializable {
    private static final long serialVersionUID = -4865946674835353955L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    int id;
//    String code;
//    String name;
    StockItem item = null;
    List<StockUnit> data = new ArrayList<>();

    int currValue;
    int currVolume;
    int minValue=100000000;   // 종가기준
    int maxValue=0;   // 종가기준
    int minVolume=100000000;
    int maxVolume=0;
    int avgVolume=0;

    // 스코어 기준
    float widthValue;    // (max/min) : 클수록 좋음
    float currValuePos;  // (curr-min)/(max-min) : 작을수록 좋음 (0.1)
    float currValuePos10;   // 최근 10일 위치 (CP_BASE)

    // 변화량 : 클수록 좋음
    float ratioHL=0;   // sum(고가/저가)
    float ratioCC=0;   // sum(abs(1-종가1/종가2))
    float ratioCO=0;   // sum(abs(1-종가/시가))
    float ratioVV=0;   // sum(abs(1-거래량1/거래량2))
    float ratioOL=0;   // sum(시가/저가) : 클수록 좋음

    // 최근 변화량
    float ratioHL2=0;   // 최근 10일
    float ratioCO2=0;   // 최근 10일
    float ratioOL2=0;   // 최근 10일
    float ratioVV2=0;   // 최근 10일
    float ratioVV3=0;   // 최근 10일과 과거 50일간의 차이
    float ratioCC2=0;   // 최근 10일 (Deprecated)
    float ratioLC2=0;   // 최근 10일
    float ratioHC=0;   // 최근 10일

    // 최종
    float ratioLL=0;
    boolean isFlag1 = true;
    boolean isFlagDown = false;
    boolean isFlagClose = false;
    boolean isFlagVolume = false;

    boolean isUp = true;
    int isUp1 = 0;       // +이면 UP, -이면 DOWN
    int isUp2 = 0;      // +이면 UP, -이면 DOWN (조더 과거) --> isUp2를 보고, isUp의 방향석 예측
                        // isUp2가 down이고, isUp이 down이면 곧 Up

    // 보합
    float steadyValue=0;    // 작을수록 좋음
    float descVolume=0;     // 클수록 좋음

    // 스코어 (순위기준)
    int rankWidthValue;
    int rankCurrValuePos;
    int rankCurrValuePos10;
    int rankRatioHL;
    int rankRatioCC;
    int rankRatioCO;
    int rankRatioVV;
    int rankRatioOL;
    int rankSteadyValue;
    int rankDescVolume;
    int rankRatioHL2;
    int rankRatioCO2;
    int rankRatioOL2;
    int rankRatioVV2;
    int rankRatioVV3;
    int rankRatioCC2;
    int rankRatioLC2;
    int rankRatioHC;
    int rankRatioLL;

    float ratioCC01=0; // 클수록
    float ratioOH01=0; // 클수록
    float ratioCC02=0; // 작을수록
    float ratioOL02=0; // 클수록

    float ratioCC11=0; // 작을수록
    float ratioHL11=0; // 작을수록
    float ratioCC12=0; // 클수록
    float ratioHL12=0; // 클수록

    float ratioC4C21=0; // 작을수록
    float ratioCC22=0;  // 작을수록
    float ratioVV22=0;  // 클수록
    float ratioOL22=0;  // 클수록
    float ratioOH22=0;  // 작을수록
    float ratioCC23=0;  // 클수록 or 0에 가까울수록
    float ratioVV23=0;  // 작을수록
    float ratioOH23=0;  // 클수록

    int rankRatioC4C21=0; // 작을수록
    int rankRatioCC22=0;  // 작을수록
    int rankRatioVV22=0;  // 클수록
    int rankRatioOL22=0;  // 클수록
    int rankRatioOH22=0;  // 작을수록
    int rankRatioCC23=0;  // 클수록 or 0에 가까울수록
    int rankRatioVV23=0;  // 작을수록
    int rankRatioOH23=0;  // 클수록

    int rankRatioCC01; // 클수록
    int rankRatioOH01; // 클수록
    int rankRatioCC02; // 작을수록
    int rankRatioOL02; // 클수록

    int rankRatioCC11; // 작을수록
    int rankRatioHL11; // 작을수록
    int rankRatioCC12; // 클수록
    int rankRatioHL12; // 클수록



    // 최종 스코어
    float score;

    // 특수 정보들
    boolean isNew = false;
    String isNewDate = null;
    boolean isStop = false;
    String isStopDate = null;
    boolean isSkip = false;

    public StockData(StockItem item) {
        this.item = item;
        isFlag1 = true;
    }

    public void setCrawlingData(Elements trs) {
        for (int i=0; i<trs.size(); i++) {
            StockUnit lUnit = new StockUnit();
            lUnit.setAllData(trs.get(i).select("td"));
            if (i == 0) {
                currValue = lUnit.getCloseValue();
                currVolume = lUnit.getVolume();
            }

            data.add(lUnit);
        }
    }

    public void setAllData() {
        StockUnit lUnitOld = data.get(0);
        int isSkipFlag = 0;
        int avgValue=0;

        currValuePos10 = Math.abs((float)data.get(0).getCloseValue() / data.get(3).getCloseValue() -1 -Constants.CP_BASE);
        currValuePos10 += Math.abs((float)data.get(3).getCloseValue() / data.get(6).getCloseValue() -1);
        int lValVolume = 0, lCurrValVolume = 0;
        for (int i=0; i<data.size(); i++) {
            StockUnit lUnit = data.get(i);
            lValVolume = lUnit.getCloseValue()*lUnit.getVolume();
            if (i == 0) {
                currValue = lUnit.getCloseValue();
                currVolume = lUnit.getVolume();
                if (currVolume < 1000) {
                    isStop = true; isSkip = true;
                }
                lCurrValVolume = lValVolume;
            }
            if (lValVolume < ratioCC11) ratioCC11 = lValVolume;
            if (lValVolume > ratioCC12) ratioCC12 = lValVolume;

            /* 하루치 데이터에 대한 유효성 체크 */
            // 신규 상장 종목입니다.
            if (!isNew && lUnit.getDate() == null) {
                isNew = true;
                isNewDate = lUnitOld.getDate();
                break;
            }

            // 거래 정지 종목입니다.
            if (!isStop && (lUnit.getVolume() < 10)) {
                isStop = true;
                isStopDate = lUnit.getDate();
            } else if (isStop && (lUnit.getVolume() > 10)) {
                isStop = false;
            }

            // 미대상 종목입니다.
            if (!isSkip) {
                avgValue += lUnit.getCloseValue();
                avgVolume += lUnit.getVolume();
            }

            // 값이 0이라서, 전날 데이터로 치환합니다.
            if (i > 0) lUnit.fixData(data.get(i-1));
            else lUnit.fixData(null);

            lUnitOld = lUnit;
        }
        ratioVV2 = (float)((lCurrValVolume - ratioCC11) / (ratioCC12-ratioCC11));

        avgValue = avgValue / data.size();
        avgVolume = avgVolume / data.size(); if (avgVolume < 1000) avgVolume = 1;
        if (avgVolume<Constants.SKIP_VOLUME_MIN ||
                avgValue<Constants.SKIP_VALUE_MIN || avgValue>Constants.SKIP_VALUE_MAX) {
            isSkip = true;
        }

        if (isStop) logger.info(" --> 거래중지 종목 : {} / {}", item.getName(), isStopDate);
        else if (isNew) logger.info(" --> 신규상장 종목 : {} / {}", item.getName(), isNewDate);
        else if (isSkip) logger.info(" --> 미대상 종목 : {} / {}원 / {}건", item.getName(), currValue, currVolume);
    }

    public void setAllData2nd() {
        StockUnit lUnit = null;
        StockUnit lUnitOld = null;
        int baseVolumeSum=0;
        int currValue2=0;
        int lVol3=0;
        float ratioCC3=0, ratioCCSum=0;
        int lFlagClose = 0, lFlagDown = 0, lFlagVolume = 0;

        // 스코어 values
        boolean lisFlag1 = false;
        for (int i = 0; i<data.size(); i++) {
            lUnit = data.get(i);
            if (i<(data.size()-1)) lUnitOld = data.get(i+1);
            baseVolumeSum += lUnit.getVolume();

            // 변수 초기값
            if (i == 0) {
                currValue2 = lUnit.getCloseValue();
            }

            // 변수 갱신
            int lOpen = lUnit.getOpenValue();
            int lClose = lUnit.getCloseValue();
            int lVolume = lUnit.getVolume();
            if (lClose < minValue) minValue = lClose;
            if (lClose > maxValue) maxValue = lClose;
            if (lVolume < minVolume) minVolume = lVolume;
            else if (lVolume > maxVolume) maxVolume = lVolume;

            // Ratio 계산
            ratioHL = calRatio((float)lUnit.getHighValue(), (float)lUnit.getLowValue());
            ratioCO += calRatio((float)lClose, (float)lOpen);
            ratioOL = calRatio2((float)lOpen, (float)lUnit.getLowValue());

            ratioCC = calRatio3((float)lClose, (float)lUnitOld.getCloseValue());
            ratioVV = calRatioVol((float)lVolume, (float)lUnitOld.getVolume());


            if (i == 0) {
                StockUnit lUnit4 = data.get(6);
                StockUnit lUnit44 = data.get(data.size()-10);
                ratioC4C21 = calRatio2(lClose, lUnit4.getCloseValue());  // 작을수록
                ratioCC01 = lClose / lUnit44.getCloseValue();   // 클수록
            }
            else if (i < 4) {
                ratioCC23 += -Math.abs(ratioCC-1.01);   // 클수록 or 같을수록(보합)
                ratioVV23 += lVolume / avgVolume;   // 작을수록
                ratioOH23 += calRatio2((float)lOpen, (float)lUnit.getHighValue());  // 클수록
            }
            else if (i < 15) {
                ratioCC22 += ratioCC;   // 작을수록
                ratioVV22 += lVolume / avgVolume;   // 클수록
                ratioOL22 += ratioOL;   // 클수록
                ratioOH22 += calRatio2((float)lOpen, (float)lUnit.getHighValue());  // 작을수록
            }
        }

        if (minValue == 0) ratioHL2 = 0;
        else ratioHL2 = (float)currValue / minValue;
/*
        if (lFlagDown < -1) isFlagDown = true;
        if (lFlagClose < -9) isFlagClose = true;
        if (lFlagVolume < -2) isFlagVolume = true;
*/

/*
        if (!lisFlag1) isFlag1 = false;

        if (isUp1 > 0) {
            if (isUp2 > 0) isUp = false;
        }
        else {
            if (isUp2 > 0) isUp = false;
        }
        ratioVV3 = (float)ratioVV3 / (lVol3-ratioVV3);

        widthValue = (float)maxValue/minValue;
        currValuePos = (float)Math.abs(((float)(currValue2-minValue))/(maxValue-minValue) - (float)Constants.CP_BASE);
        if (Float.isNaN(currValuePos)) currValuePos = 1;

        // 보합 : 가격은 abs()와 sum으로 작은 값, 거래량은 앞의평균치/거래량
        int baseVolumeAvg = baseVolumeSum / (data.size()-Constants.BASELINE);
        int baseValue = data.get(Constants.BASELINE).getCloseValue();
        for (int i=0; i<Constants.BASELINE_STEADY; i++) {
            lUnit = data.get(i);
            steadyValue += calRatio((float)baseValue, (float)lUnit.getCloseValue());
            descVolume += calRatioVol((float)baseVolumeAvg, lUnit.getVolume());
        }
*/
    }

    // 변화율을 1.5 이하로 계산 --> 1.5 초과는 무조건 1.5로 제한
    private float calRatio(float v1, float v2) {
        float lRet = v1>v2 ? v1/v2 : v2/v1;
        if (lRet > 1.05) return (float)1.05;
        if (Float.isNaN(lRet)) return 1;

        return lRet;
    }
    private float calRatio2(float v1, float v2) {
        float lRet = v1>v2 ? v1/v2 : v2/v1;
        if (lRet > 1.1) return (float)1.1;
        if (Float.isNaN(lRet)) return 1;

        return lRet;
    }
    private float calRatioVol(float v1, float v2) {
        float lRet = v1>v2 ? v1/v2 : v2/v1;
        if (lRet > 1.5) return (float)1.5;
        if (Float.isNaN(lRet)) return 1;

        return lRet;
    }
    private float calRatio3(float v1, float v2) {
        float lRet = v1/v2;
        if (lRet > 1.1) return (float)1.1;
        if (Float.isNaN(lRet)) return 1;

        return lRet;
    }

    // 최종 스코어 계산 (분자가 클수록 비중이 큰거임)
    public void calScore() {
        score = 100000
                -rankWidthValue*Constants.SCORE_WV - rankCurrValuePos*Constants.SCORE_CP
                -rankRatioHL*Constants.SCORE_HL - rankRatioCC*Constants.SCORE_CC -rankRatioCO*Constants.SCORE_CO - rankRatioVV*Constants.SCORE_VV - rankRatioOL*Constants.SCORE_OL
                -rankRatioHL2*Constants.SCORE_HL2 - rankRatioCO2*Constants.SCORE_CO2 -rankRatioOL2*Constants.SCORE_OL2 -rankRatioVV2*Constants.SCORE_VV2 -rankRatioCC2*Constants.SCORE_CC2
                -rankSteadyValue*Constants.SCORE_SV - rankDescVolume*Constants.SCORE_DV;
    }
    public void calScore2() {
        score = 100000
                -rankRatioHC*Constants.SCORE_HC -rankRatioLL*Constants.SCORE_LL -rankRatioVV3*Constants.SCORE_VV3;
//                -rankRatioHC*Constants.SCORE_HC -rankRatioOL2*Constants.SCORE_OL2 -rankRatioLC2*Constants.SCORE_LC2 -rankRatioVV3*Constants.SCORE_VV3 -rankRatioCO2*Constants.SCORE_CO2;
//                -rankRatioHC*Constants.SCORE_HC -rankRatioOL2*Constants.SCORE_OL2 -rankRatioLC2*Constants.SCORE_LC2 -rankRatioVV3*Constants.SCORE_VV3 -rankRatioCO2*Constants.SCORE_CO2;
    }
    public void calScore3() {
        score = 100000
                -rankWidthValue*Constants.SCORE_WV - rankCurrValuePos*Constants.SCORE_CP;
//                -rankRatioHC*Constants.SCORE_HC -rankRatioOL2*Constants.SCORE_OL2 -rankRatioLC2*Constants.SCORE_LC2 -rankRatioVV3*Constants.SCORE_VV3;
    }

    public void calScore01() {
        score = 100000
                -rankRatioCC01*100 -rankRatioOH01*30;
    }
    public void calScore02() {
        score = 100000
                -rankRatioCC02*100 -rankRatioOL02*30;
    }
    public void calScore11() {
        score = 100000
                -rankRatioCC11*100;// -rankRatioHL11*40;
    }
    public void calScore12() {
        score = 100000
                -rankRatioCC12*100 -rankRatioHL12*40;
    }
    public void calScore5() {
        score = 100000
                -rankRatioCC2*Constants.SCORE_CC2 -rankRatioVV2*Constants.SCORE_VV2;
    }

    public void calScore22() {
        score = 100000
                -rankRatioCC22*100 -rankRatioVV22*70 -rankRatioOL22*50 -rankRatioOH22*50;
    }
    public void calScore23() {
        score = 100000
                -rankRatioCC23*100 -rankRatioVV23*70 -rankRatioOH23*50;
    }
    public void calScore24() {
        score = 100000
                -rankRatioC4C21*100 -rankRatioCC01*70;
    }


    public void writeExcelHeader(XSSFRow curRow) {
        curRow.createCell(0).setCellValue("코드");
        curRow.createCell(1).setCellValue("이름");
        curRow.createCell(2).setCellValue("신규");
        curRow.createCell(3).setCellValue("중지");

        curRow.createCell(4).setCellValue("현재가");
        curRow.createCell(5).setCellValue("현재거래량");
        curRow.createCell(6).setCellValue("최저가");
        curRow.createCell(7).setCellValue("최고가");
        curRow.createCell(8).setCellValue("최저거래량");
        curRow.createCell(9).setCellValue("최고거래량");

        curRow.createCell(10).setCellValue("스코어");

        curRow.createCell(11).setCellValue("저평가");
        curRow.createCell(12).setCellValue("게시판");
        curRow.createCell(13).setCellValue("기업정보");
    }

    public void writeExcelRow(XSSFRow curRow, String PPer, String PBoard, String PCo) {
        curRow.createCell(0).setCellValue(item.getCode());
        curRow.createCell(1).setCellValue(item.getName());
        curRow.createCell(2).setCellValue(isNew);
        curRow.createCell(3).setCellValue(isStop);

        curRow.createCell(4).setCellValue(currValue);
        curRow.createCell(5).setCellValue(currVolume);
        curRow.createCell(6).setCellValue(minValue);
        curRow.createCell(7).setCellValue(maxValue);
        curRow.createCell(8).setCellValue(minVolume);
        curRow.createCell(9).setCellValue(maxVolume);

        curRow.createCell(10).setCellValue(score);

        curRow.createCell(11).setCellValue(PPer);
        curRow.createCell(12).setCellValue(PBoard);
        curRow.createCell(13).setCellValue(PCo);
    }

    public void setAllData2nd_old() {
        StockUnit lUnit = null;
        StockUnit lUnitOld = null;
        int baseVolumeSum=0;
        int currValue2=0;
        int lVol3=0;
        float ratioCC3=0, ratioCCSum=0;
        int lFlagClose = 0, lFlagDown = 0, lFlagVolume = 0;

        // 스코어 values
        boolean lisFlag1 = false;
        for (int i = 0; i<data.size(); i++) {
            lUnit = data.get(i);
            if (i<(data.size()-1)) lUnitOld = data.get(i+1);
            baseVolumeSum += lUnit.getVolume();

            // 변수 초기값
            if (i == 0) {
                currValue2 = lUnit.getCloseValue();
            }

            // 변수 갱신
            int lOpen = lUnit.getOpenValue();
            int lClose = lUnit.getCloseValue();
            int lVolume = lUnit.getVolume();
            if (lClose < minValue) minValue = lClose;
            if (lClose > maxValue) maxValue = lClose;
            if (lVolume < minVolume) minVolume = lVolume;
            else if (lVolume > maxVolume) maxVolume = lVolume;

            // Ratio 계산
            ratioHL = calRatio((float)lUnit.getHighValue(), (float)lUnit.getLowValue());
            ratioCO += calRatio((float)lClose, (float)lOpen);
            ratioOL = calRatio((float)lOpen, (float)lUnit.getLowValue());

            ratioCC = calRatio3((float)lClose, (float)lUnitOld.getCloseValue());
            ratioVV += calRatioVol((float)lVolume, (float)lUnitOld.getVolume());

            lVol3 += lVolume;
/*
            if (i < Constants.BASELINE2) {
                ratioCO2 = ratioCO;
                ratioCC2 = ratioCC;
                ratioHL2 = ratioHL;
                ratioOL2 = ratioOL;
                ratioVV2 = ratioVV;
                ratioVV3 = lVol3;
                ratioLC2 += calRatio((float)lUnit.getLowValue(), (float)lClose);
                ratioHC += calRatio2((float)lUnitOld.getHighValue(), (float)lClose);
                ratioLL += (float)(lOpen+lClose-lUnit.getLowValue()*2 -Math.abs(lOpen-lClose))/lOpen;
            }
            if (i < 3) {
                if (lClose > lOpen) isUp1++;
                else isUp1--;
            }
            if (i >= 3 && i < 7) {
                if (lClose > lOpen) isUp2++;
                else isUp2--;
            }
*/

/*
            ratioCC3 = calRatio3((float)lClose, (float)lUnitOld.getCloseValue());
            if (i>=0 && i<=2) {
                if (isFlag1) {
                    if (ratioCC3 > 1.025) isFlag1 = false;
                    else {
                        ratioCCSum += ratioCC3;
                        if (i == 2) {
                            if (ratioCCSum > 2.99) isFlag1 = false;
                            ratioCCSum = ratioCC3;
                        }
                    }
                }
            }
            else if (isFlag1 && i <= 7) {
                if (!lisFlag1) {
                    ratioCCSum += ratioCC3;
                    if (ratioCC3 < 0.95) {
                        if (ratioCCSum < 1.88) lisFlag1 = true;
                    }
                    if (!lisFlag1) {
                        ratioCCSum = ratioCC3;
                    }
                }
            }
*/

/*
            if (i < 10) {
                if (i>=1 && ratioCC3 <= 0.95) {
                    StockUnit lUnitOld2 = data.get(i-1);
                    float lRatio = calRatio3((float)lUnitOld2.getCloseValue(), (float)lClose);
                    if (lRatio <= 0.95) {
                        isFlagDown = true;
                    }
                }
            }
*/
/*
            if (i>=(Constants.BASELINE+3) && i<(Constants.BASELINE+15)) {
                if (ratioCC3 < 1) {
                    StockUnit lUnitOld2 = data.get(i+2);
                    float lRatio = calRatio3((float)lClose, (float)lUnitOld2.getCloseValue());
                    if (lRatio < 0.93) lFlagDown--;
                }
            }

            if (i < (Constants.BASELINE+9)) {
                if (ratioCC3 < 1.01 && ratioCC3 > 0.99) lFlagClose = lFlagClose-3;
                else if (ratioCC3 < 1.02 && ratioCC3 > 0.975) lFlagClose = lFlagClose-2;
                else if (ratioCC3 < 1.03 && ratioCC3 > 0.96) lFlagClose--;
                else lFlagClose++;
            }

            if (i < (Constants.BASELINE+7)) {
                if (lVolume < avgVolume*0.5) lFlagVolume--;
            }
    int rankRatioCC01; // 클수록
    int rankRatioOH01; // 클수록
    int rankRatioCC02; // 작을수록
    int rankRatioOL02; // 클수록

    int rankRatioCC11; // 작을수록
    int rankRatioHL11; // 작을수록
    int rankRatioCC12; // 클수록
    int rankRatioHL12; // 클수록
*/
            if (i >= 0 && i <= 2) {
//                ratioCC02 += -ratioCC; // 작을수록
                ratioOL02 += -ratioOL; // 클수록
            }
            else if (i >= 3 && i < 8) {
                ratioCC01 += -ratioCC; // 클수록
                ratioOH01 += -calRatio((float)lOpen, (float)lUnit.getHighValue()); // 클수록
            }

            if (i < 2) {
                ratioCC12 += ratioCC*ratioVV; // 클수록
                ratioHL12 += ratioHL; // 클수록
            }
            else if (i >= 2 && i < 10) {
                ratioCC11 += calRatio2((float)lClose, (float)lUnitOld.getCloseValue()); // 작을수록
                ratioHL11 += ratioHL; // 작을수록
            }

            if (i == 0) {
                ratioVV3 = lUnit.getVolume()*lClose;
            }

            if (i == 0) {
                StockUnit lUnit4 = data.get(3);
                this.ratioCC02 = calRatio(lUnit.getCloseValue(), lUnit4.getCloseValue());
            }
        }
/*
        if (lFlagDown < -1) isFlagDown = true;
        if (lFlagClose < -9) isFlagClose = true;
        if (lFlagVolume < -2) isFlagVolume = true;
*/

        if (!lisFlag1) isFlag1 = false;

        if (isUp1 > 0) {
            if (isUp2 > 0) isUp = false;
        }
        else {
            if (isUp2 > 0) isUp = false;
        }
        ratioVV3 = (float)ratioVV3 / (lVol3-ratioVV3);

        widthValue = (float)maxValue/minValue;
        currValuePos = (float)Math.abs(((float)(currValue2-minValue))/(maxValue-minValue) - (float)Constants.CP_BASE);
        if (Float.isNaN(currValuePos)) currValuePos = 1;

        // 보합 : 가격은 abs()와 sum으로 작은 값, 거래량은 앞의평균치/거래량
        int baseVolumeAvg = baseVolumeSum / (data.size()-Constants.BASELINE);
        int baseValue = data.get(Constants.BASELINE).getCloseValue();
        for (int i=0; i<Constants.BASELINE_STEADY; i++) {
            lUnit = data.get(i);
            steadyValue += calRatio((float)baseValue, (float)lUnit.getCloseValue());
            descVolume += calRatioVol((float)baseVolumeAvg, lUnit.getVolume());
        }
    }

}

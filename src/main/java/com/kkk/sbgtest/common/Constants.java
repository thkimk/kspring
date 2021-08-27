package com.kkk.sbgtest.common;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;

public class Constants {
    public static final int BASELINE = 0;            // 스코어 계산 기준일 (BASE일부터 뒤로,, 보통은 0값임)
    public static final int BASELINE_STEADY = 10;    // 보합 계산 기준일 (오늘부터 BASE일까지)
    public static final int SKIP_VALUE_MIN = 900;
    public static final int SKIP_VALUE_MAX = 200000;
    public static final int SKIP_VOLUME_MIN = 20000;
    public static final float CP_BASE = 0.03f;   // CP 기준치

    public static String STOCK_FILE = "d:/stock/stockDatas_0000.obj";
    public static String STOCK_RESULT_FILE1 = "d:/stock/stockDatas_0001.obj";
    public static String STOCK_RESULT_FILE2 = "d:/stock/stockDatas_0002.obj";
    public static String STOCK_RESULT_FILE = "d:/stock/stockDatas_0000.obj";

    // 스코어 비중(가중치)
    public static int SCORE_WV = 200;   // 진폭
    public static int SCORE_CP = 100;   // 현재 위치 (낮을수록 좋게 잡혀있음)
    public static int SCORE_HL = 80;
    public static int SCORE_CC = 20;
    public static int SCORE_CO = 30;
    public static int SCORE_VV = 60;
    public static int SCORE_OL = 40;
    public static int SCORE_SV = 120;
    public static int SCORE_DV = 60;
    public static int SCORE_HL2 = 60;
    public static int SCORE_CO2 = 60;
    public static int SCORE_OL2 = 60;
    public static int SCORE_VV2 = 60;
    public static int SCORE_VV3 = 60;
    public static int SCORE_CC2 = 60;   // 출렁이 시작한 후, 종가율이 약간 상승한게 좋음 --> 떨어진거는 더 떨어짐
    public static int SCORE_LC2 = 60;
    public static int SCORE_HC = 60;
    public static int SCORE_LL = 60;
    public static int BASELINE2 = 10;

    public static final int SCORE_SEL_COUNT = 30;
    public static final int SCORE_SEL_COUNT2 = 50;
    public static final int CRAWLING_COUNT = 6; // 6*10 일치
    public static int STOCK_TYPE = 1; // 6*10 일치

    public Constants() {
        SCORE_HC = -80;
        SCORE_OL2 = 0;
        SCORE_LC2 = 100;
        SCORE_CC2 = 100;
        SCORE_VV2 = 100;
        SCORE_VV3 = 70;
        SCORE_CO2 = -60;
        SCORE_LL = 300;

        SCORE_WV = 450;
        SCORE_CP = 1800;

        STOCK_TYPE = 2;
    }

    public static final String[] BOARD_WORDINGS = {"무덤", "상폐", "잡주", "무덤", "증자", "안녕", "곡소리",
        "유증", "개미", "쓰레기", "부도", "장투", "드디어", "손절", "바닥", "알바", "외인", "기관", "들어", "조정",
        "손실", "노답", "안티", "거품", "쇼크", "답답", "IR", "관리", "기회", "마지막", "타이밍"};

    public static void initStockFile() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        STOCK_FILE = String.format("d:/stock/stockDatas_%02d%02d.obj", month, day);
        STOCK_RESULT_FILE = String.format("d:/stock/stock_%02d%02d.xlsx", month, day);
        STOCK_RESULT_FILE1 = String.format("d:/stock/stock_%02d%02d_1.xlsx", month, day);
        STOCK_FILE = "d:/stock/stockDatas_0801.obj";
    }
}

package com.kkk.sbgtest.common;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CUtils {
    /**
     * 평균, 분산, 표준편차 구하기
     * double[] data = {3,3,3,4,5,6,7,8,8,8};
     * double[] result = univerate(data);
     * @param data
     * @return
     */
    private double[] univerate(double[] data) {
        double[] statistic = new double[3];
        double sum = 0;
        double sum_2 = 0;
        int n = data.length;
        for (int i=0; i<n; i++) {
            sum += data[i];
            sum_2 += data[i]*data[i];
        }
        statistic[0] = sum / n;     // 평균
        statistic[1] = (sum_2 -n*statistic[0]*statistic[0])/n -1;   // 분산
        statistic[2] = Math.sqrt(statistic[1]);     // 표준편차
        return statistic;
    }

    @Test
    public void testQTime() {
        Date time = new Date(1625038260000L);
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        System.out.println("## : "+ format1.format(time));

        final long startTime = System.currentTimeMillis();
        SimpleDateFormat format2 = new SimpleDateFormat( "HH:mm");
        System.out.println("## : "+ format2.format(startTime));

    }

    public void stringSplit() {
        String lArg = "111 222 333";
        String[] lNames = lArg.split(" ");
        int[] lNums = new int[lNames.length];

        for (int i=0; i< lNames.length; i++) {
            lNums[i] = Integer.parseInt(lNames[i]);
        }
        System.out.println(lNames.length);
    }

}

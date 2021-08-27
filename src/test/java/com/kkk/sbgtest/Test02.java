package com.kkk.sbgtest;

import com.kkk.sbgtest.stock.StockData;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Test02 {
    @Test
    public void test01_() {
        try {
            System.out.println("start..");
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("ing..");
//            String input = br.readLine();
            String input = "aaaa";
            System.out.println("Hello Goorm! Your input is " + input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test02() {
//        int lCount = 3;
//        int[] lNums = { 1, 2, 3 };
        int lCount = 5;
        int[] lNums = { 1, 3, 5, 7, 9 };

        // 원천값을 가장 높은 점수를 얻을 수 있는 리스트로 소팅 구성
        Arrays.sort(lNums, 1, lNums.length);
        int lTmp = lNums[0];
        for (int i=0; i<lNums.length; i++) {
            if (((i+1)%2) == 0) {
                for (int j=0; j<=i; j++) {
                    if (j == 0) lTmp = lNums[j];
                    else if (j == i) {
                        lNums[j-1] = lNums[j];
                        lNums[j] = lTmp;
                    }
                    else if (((j+1)%2) == 0) continue;
                    else {
                        lNums[j-2] = lNums[j];
                    }
                }
            }
        }

        /* 워커라운드 임시안 */
        if (true) {
            int lSize = lCount / 2;
            int[] lNumsCal = new int[lSize];
            for (int i = (lNums.length-1), j=0; i > 0; i--) {
                if (((i+1) % 2) == 0) {
                    lNumsCal[j++] = lNums[i];
                }
            }
            for (int i = 0, j=0; i < lNums.length; i++) {
                if (((i+1) % 2) == 0) {
                    lNums[i] = lNumsCal[j++];
                }
            }
        }

        // 최대값 케이스 시도에 대한 결과값 확인
        int lResult = test02_proc(lCount, lNums);
        System.out.println("result is "+ lResult);
    }
    /**
     * 한번의 시도에 대한 결과값
     */
    int test02_proc(int count, int[] nums) {
        int lResult = 0;
        for (int i=0; i<count; i++) {
            int lUnitResult = nums[i] * (i+1);
            if (((i+1)%2) == 0) lUnitResult = lUnitResult*(-1);

            lResult += lUnitResult;
        }
        return lResult;
    }

    @Test
    public void test03() {
        int lCount = 6;
        int[] lIos = { 2, 1, 3, 4, -3, -4, -1, -2, 5, -5, 1, 6, -1, -6 };
        int lConfirm = 1;

        int[] lConfirms = test03_proc(lCount, lIos, lConfirm);
        for (int i=0; i<lCount; i++) {
            if ((i+1) == lConfirm) continue;
            if (lConfirms[i] == 2) System.out.println(i+1);
        }

    }
    /**
     * 확진자가 건물에 있는 기간을 확인하고, 해당 기간에 건물에 있었던 사람 확인
     */
    int[] test03_proc(int count, int[] lIos, int confirm) {
        int[] lResults = new int[count]; // 0: out, 1: in, 2: 밀접접촉자
        for (int i=0; i<count; i++) lResults[i] = 0;

        boolean lConfirmFlag = false;
        for (int i=0; i<lIos.length; i++) {
            if (Math.abs(lIos[i]) == confirm) {
                if (lIos[i] > 0) lConfirmFlag = true;
                else lConfirmFlag = false;
            }

            // 이미 밀접접촉자면, skip
            if (lResults[Math.abs(lIos[i])-1] == 2) continue;

            // 건물 출입자 확인
            if (lIos[i] > 0) {
                lResults[lIos[i]-1] = 1;
            } else if (lIos[i] < 0) {
                lResults[-lIos[i]-1] = 0;
            }

            // 지금 건물은 위험 상태
            if (lConfirmFlag) {
                for (int j=0; j<count; j++) {
                    if (lResults[j] == 2) continue;
                    else if (lResults[j] == 1) lResults[j] = 2;
                }
            }
        }
        return lResults;
    }


    @Test
    public void test01() {
//        int lCount1=4, lCount2=2;
//        String[] lOnes = { "RA", "LW", "LD", "JX"};
//        String[][] lRels = {{"LW", "JX"}, {"LD", "JX"}};
        int lCount1=7, lCount2=8;
        String[] lOnes = { "A", "B", "C", "D", "E", "P", "Q"};
        String[][] lRels = {{"P", "A"}, {"P", "B"}, {"P", "C"}, {"P", "D"}, {"Q", "B"}, {"Q", "C"}, {"Q", "D"}, {"Q", "E"}};

        List<Model1> lModels = new ArrayList<>();
        for (int i=0; i<lOnes.length; i++) {
            Model1 lModel = new Model1(lOnes[i]);

            for (int j=0; j<lRels.length; j++) {
                for (int k=0; k<lRels[j].length; k++) {
                    if (k==0) {
                        if (lModel.getOne().equals(lRels[j][0])) {
                            lModel.getRels().add(lRels[j][1]);
                        }
                    }
                }
            }

            lModels.add(lModel);
        }

        Model1 lResult1, lResult2;
        int lSameCount = 0;
        for (Model1 lModel1 : lModels) {
            for (Model1 lModel2 : lModels) {
                if (lModel1 == lModel2) continue;
                if (lModel1.getRels().size() == 0) continue;
                if (lModel2.getRels().size() == 0) continue;

                System.out.println(lModel1.getOne()+ " "+ lModel2.getOne());
                List<String> lRels1 = lModel1.getRels();
                List<String> lRels2 = lModel2.getRels();
                for (String lRel1 : lRels1) {
                    for (String lRel2 : lRels2) {
                        if (lRel1.equals(lRel2)) lSameCount++;
                    }
                }
                System.out.println(lSameCount);
            }
            if (lSameCount > 0) break;
        }
    }

    public class Model1 {
        String one;
        List<String> rels;

        public Model1(String p_one) {
            one = p_one;
            rels = new ArrayList<String>();
        }

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

        public List<String> getRels() {
            return rels;
        }

        public void setRels(List<String> rels) {
            this.rels = rels;
        }
    }


    public void testArg() {
        String lArg = "111 222 333";
        String[] lNames = lArg.split(" ");
        int[] lNums = new int[lNames.length];

        for (int i=0; i< lNames.length; i++) {
            lNums[i] = Integer.parseInt(lNames[i]);
        }
        System.out.println(lNames.length);
    }
}

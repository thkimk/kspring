package com.kkk.sbgtest;

public class NotifyData {
    /*
     * 두 스레드가 공유하는 클래스
     */
    public final static int MAX = 100;
    private int cnt = 0;

    // wait(), notify()를 사용하기 위해서는 synchronized가 적용된 메소드만 가능

    public synchronized void doWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // cnt가 60일 때 notify();
    public synchronized void doNotify() {
        notify();
    }

    @Override
    public String toString() {
        return "cnt : " + cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}

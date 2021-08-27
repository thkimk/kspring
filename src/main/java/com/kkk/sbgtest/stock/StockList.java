package com.kkk.sbgtest.stock;

public enum StockList {
    ST_001("000270", "기아차"),
    ST_002("316140", "우리은행"),
    ;

    String code;
    String name;

    StockList(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }

}

package com.kkk.sbgtest.model;

public enum EnumData {
    REQUEST_AMENITY("st00001101", "pp00001101") {
        public String getProperty(String value) {
            return "RET_"+ value;
        }
    };

    private String svcType;
    private String propertyCode;

    private EnumData(String svcType, String proprtyCd) {
        this.svcType = svcType;
        this.propertyCode = proprtyCd;
    }

    public String getSvcType() {
        return svcType;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public String getProperty(String value) {
        return value;
    }
}

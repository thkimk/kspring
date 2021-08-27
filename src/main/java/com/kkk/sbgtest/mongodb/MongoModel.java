package com.kkk.sbgtest.mongodb;

import lombok.Data;

@Data
public class MongoModel {
    private String name;
    private String job;

    public MongoModel(String name, String job) {
        this.name = name;
        this.job = job;
    }

    @Override
    public String toString() {
        return "name is " + name + " job is " + job;
    }
}

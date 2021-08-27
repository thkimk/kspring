package com.kkk.sbgtest.mongodb;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Repository
public class MongoMyRepository {
    @Setter(onMethod_ = {@Autowired})
    private MongoTemplate mongoTemplate;

    public void inputDataInsert(Object input) {
        Map<String, Object> inputDataMap = new HashMap<String, Object>();
        inputDataMap.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        inputDataMap.put("inputData", input);
        Map<String, Object> lRet = mongoTemplate.insert(inputDataMap, "input_data");
        System.out.println(lRet.toString());
    }

    public void outputDataInsert(Object output) {
        Map<String, Object> outputDataMap = new HashMap<String, Object>();
        outputDataMap.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        outputDataMap.put("outputData", output);
        mongoTemplate.insert(outputDataMap, "output_data");
    }

}
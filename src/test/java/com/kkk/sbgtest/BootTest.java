package com.kkk.sbgtest;

import com.kkk.sbgtest.mongodb.MongoDBRepository;
import com.kkk.sbgtest.mongodb.MongoModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class BootTest {
    @Autowired
    private MongoDBRepository mongoDBRepository;

    @Test
    public void printProjectData() {
        mongoDBRepository.insert(new MongoModel("aaa","bbb"));
        System.out.println("thkim"+ mongoDBRepository.findAll());
    }



}

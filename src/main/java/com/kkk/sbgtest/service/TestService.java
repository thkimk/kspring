package com.kkk.sbgtest.service;


import com.kkk.sbgtest.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TestService {
    @Autowired
    TestDao testDao;

    public void testMariadb() {
        testDao.getMariadb();
    }

    public void selectData() {
        HashMap<Object,Object> lParam = new HashMap<>();
        List<HashMap<Object, Object>> lData = testDao.selectData(lParam);
    }
}

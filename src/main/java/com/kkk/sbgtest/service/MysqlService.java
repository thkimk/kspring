package com.kkk.sbgtest.service;

import com.kkk.sbgtest.dao.MysqlDao;
import com.kkk.sbgtest.dao.MysqlIfDao;
import com.kkk.sbgtest.model.B2bBasData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MysqlService {
//    @Autowired
//    MysqlDao mysqlDao;

//    @Autowired
//    MysqlIfDao mysqlIfDao;

    public  MysqlService() {}

//    public void selectlist() {
//        try {
//            Map lParams = new HashMap();
//            List<Map<String, Object>> lMap = mysqlDao.selectlist(lParams);
//            System.out.println("## MysqlService.java [selectlist] "+ lMap.size());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void selectB2bBas() {
//        B2bBasData lParam = new B2bBasData();
//        List<B2bBasData> lData = mysqlIfDao.selectB2bBas(lParam);
//        System.out.println("## MysqlService.java [selectB2bBas] size: "+ lData.size());
    }
}

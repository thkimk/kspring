package com.kkk.sbgtest.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//@Repository
public class MysqlDao {
    @Autowired
    @Qualifier("thirdSqlSessionTemplate")
    private SqlSession session;
    private String namespace = "com.kkk.sbgtest.dao.mysql.";

    public List<Map<String,Object>> selectlist(Object params) throws Exception {
        return session.selectList(namespace+ "selectlist", params);
    }
}

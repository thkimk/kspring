package com.kkk.sbgtest.dao;


import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface Test2Dao {
    // mappers : com.kkk.sbgtest.dao.TestDao.test1
    public ArrayList<HashMap> test1();

    public ArrayList<HashMap> getMariadb();

    public List<HashMap<Object, Object>> selectData(HashMap<Object, Object> vo);

    public int deleteData();

}

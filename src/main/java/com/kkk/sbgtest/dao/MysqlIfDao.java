package com.kkk.sbgtest.dao;

import com.kkk.sbgtest.model.B2bBasData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MysqlIfDao {
    public List<B2bBasData> selectB2bBas(B2bBasData params);
}

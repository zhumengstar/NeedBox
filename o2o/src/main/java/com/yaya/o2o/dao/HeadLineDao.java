package com.yaya.o2o.dao;

import com.yaya.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {

    //根据传入的查询条件(头条名)查询头条
    List<HeadLine> queryHeadLine(@Param("headLineCondition")HeadLine headLineCondition);

    //根据头条id返回唯一的头条信息

}

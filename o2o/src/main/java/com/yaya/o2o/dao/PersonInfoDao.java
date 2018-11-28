package com.yaya.o2o.dao;

import com.yaya.o2o.entity.PersonInfo;

public interface PersonInfoDao {

    //通过用户ID查询用户
    PersonInfo queryPersonInfoById(long userId);

    //添加用户信息
    int insertPersonInfo(PersonInfo personInfo);

}

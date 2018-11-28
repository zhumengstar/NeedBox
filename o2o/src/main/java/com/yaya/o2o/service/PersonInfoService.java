package com.yaya.o2o.service;

import com.yaya.o2o.entity.PersonInfo;

public interface PersonInfoService {

    //根据用户id获取personInfo信息
    PersonInfo getPersonInfoById(Long userId);
}

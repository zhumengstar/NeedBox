package com.yaya.o2o.dao;

import com.yaya.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {

    //通过帐号和密码查询对应信息,登录用
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username,
                                         @Param("password") String password);

    //通过用户ID查询对应Localauth
    LocalAuth queryLocalByUserId(@Param("userId")long userId);

    //添加平台帐号
    int insertLocalAuth(LocalAuth localAuth);

    //通过userId,username,password更改密码
    int updateLocalAuth(@Param("userId") Long userId,
                        @Param("username") String username,
                        @Param("password") String password,
                        @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);

    int queryLocalByUserName(@Param("username") String username);
}

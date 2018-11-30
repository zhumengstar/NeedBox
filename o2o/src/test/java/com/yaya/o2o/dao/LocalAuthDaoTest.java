package com.yaya.o2o.dao;

import com.yaya.o2o.BaseTest;
import com.yaya.o2o.entity.LocalAuth;
import com.yaya.o2o.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class LocalAuthDaoTest extends BaseTest {

    @Autowired
    private LocalAuthDao localAuthDao;

    private static final String username = "testusername";
    private static final String password = "testpassword";

    @Test
    public void testInsertLocalAuth() throws Exception {
        //新增一条平台帐号信息
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        //给平台帐号绑定上用户信息
        localAuth.setPersonInfo(personInfo);
        //设置上用户名和密码
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testQueryLocalByUserNameAndPwd() throws Exception {
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
        assertEquals("测试", localAuth.getPersonInfo().getName());
    }

    @Test
    public void testQueryLocalByUserId() throws Exception {
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        assertEquals("测试", localAuth.getPersonInfo().getName());
    }

    @Test
    public void testUpdateLocalAuth() throws Exception {
        Date now = new Date();
        String newPassword = password + "new";
        int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, newPassword, now);
        assertEquals(1, effectedNum);
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        System.out.println(localAuth.getPassword());

    }

}

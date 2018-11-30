package com.yaya.o2o.service;

import com.yaya.o2o.BaseTest;
import com.yaya.o2o.dto.LocalAuthExecution;
import com.yaya.o2o.entity.LocalAuth;
import com.yaya.o2o.entity.PersonInfo;
import com.yaya.o2o.enums.LocalAuthStateEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class LocalAuthServiceTest extends BaseTest {

    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testBindLocalAuth() {
        //新增一条平台帐号
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        localAuth.setPersonInfo(personInfo);
        String username = "testusername";
        String password = "testpassword";
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(), lae.getState());
        //通过userId找到新增的localAuth
        localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
        System.out.println(localAuth.getPersonInfo().getName());
        System.out.println(localAuth.getPassword());
    }

    @Test
    public void testModifyLocalAuth() {
        long userId = 1L;
        String username = "testusername";
        String password = "testpassword";
        String newPassword = "testnewpassword";
        //修改对应密码
        LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(), lae.getState());
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
        System.out.println(localAuth.getPersonInfo().getName());
    }
}

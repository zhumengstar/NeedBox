package com.yaya.o2o.service;

import com.yaya.o2o.BaseTest;
import com.yaya.o2o.dto.WechatAuthExecution;
import com.yaya.o2o.entity.PersonInfo;
import com.yaya.o2o.entity.WechatAuth;
import com.yaya.o2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class WeChatAuthServiceTest extends BaseTest {
    @Autowired
    private WeChatAuthService weChatAuthService;

    @Test
    public void testRegister() {
        //新增一条微信号
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        String openId = "testopenid";
        //给微信账号设置用户信息,但不设置用户ID
        //希望创建微信帐号的时候自动创建用户信息
        personInfo.setCreateTime(new Date());
        personInfo.setName("ceshi");
        personInfo.setUserType(1);
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId(openId);
        wechatAuth.setCreateTime(new Date());
        WechatAuthExecution wechatAuthExecution = weChatAuthService.register(wechatAuth);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wechatAuthExecution.getState());

        wechatAuth = weChatAuthService.getWechatAuthByOpenId(openId);
        System.out.println(wechatAuth.getPersonInfo().getName());
    }
}
